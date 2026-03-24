package com.example.tablero.servicio.impl;

import com.example.tablero.entidades.dtos.entrada.EntregableDtoEntrada;
import com.example.tablero.entidades.dtos.salida.EntregableDtoSalida;
import com.example.tablero.entidades.entidades.EntregablesEntity;
import com.example.tablero.entidades.entidades.TareaEntity;
import com.example.tablero.entidades.entidades.enums.TipoEntregable;
import com.example.tablero.mapper.EntregableMapper;
import com.example.tablero.repositorio.EntregablesRepositorio;
import com.example.tablero.repositorio.TareaRepositorio;
import com.example.tablero.servicio.interfaces.EntregableI;
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class EntregableServiceImpl implements EntregableI {

    private EntregablesRepositorio repositorio;
    private EntregableMapper mapper;
    private Path rootLocation = Paths.get("uploads");
    private TareaRepositorio tareasRepositorio;

    public EntregableServiceImpl(EntregablesRepositorio repositorio, EntregableMapper mapper,
            TareaRepositorio tareasRepositorio) {
        this.repositorio = repositorio;
        this.mapper = mapper;
        this.tareasRepositorio = tareasRepositorio;
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new TableroExcepcion("No se pudo inicializar la carpeta de almacenamiento",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public EntregablesEntity guardarEntregable(EntregableDtoEntrada entregableDto) {
        EntregablesEntity entity = new EntregablesEntity();

        // Validación del tipo — aislada de todo lo demás
        TipoEntregable tipo;
        try {
            tipo = TipoEntregable.valueOf(entregableDto.getTipoEntregable().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TableroExcepcion("Tipo de entregable inválido: " + entregableDto.getTipoEntregable(),
                    HttpStatus.BAD_REQUEST);
        }

        entity.setTipoEntregable(tipo);

        // Manejo del archivo — su IOException queda contenida aquí
        if (tipo == TipoEntregable.ARCHIVO) {
            if (entregableDto.getArchivo() == null || entregableDto.getArchivo().isEmpty()) {
                throw new TableroExcepcion("Se requiere un archivo para este tipo de entregable",
                        HttpStatus.BAD_REQUEST);
            }
            try {
                String fileName = UUID.randomUUID() + "_" + entregableDto.getArchivo().getOriginalFilename();
                Files.copy(entregableDto.getArchivo().getInputStream(), this.rootLocation.resolve(fileName));
                entity.setNombreArchivo(entregableDto.getArchivo().getOriginalFilename());
                entity.setRuta(this.rootLocation.resolve(fileName).toString());
                entity.setRecurso(fileName);
            } catch (IOException e) {
                throw new TableroExcepcion("Error al guardar el archivo", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (tipo == TipoEntregable.ENLACE) {
            entity.setRecurso(entregableDto.getEnlace());
        }

        // Asociación con tarea — una sola consulta, UUID protegido
        if (entregableDto.getIdTarea() != null) {
            UUID tareaId;
            try {
                tareaId = UUID.fromString(entregableDto.getIdTarea());
            } catch (IllegalArgumentException e) {
                throw new TableroExcepcion("ID de tarea con formato inválido", HttpStatus.BAD_REQUEST);
            }

            TareaEntity tarea = tareasRepositorio.findById(tareaId)
                    .orElseThrow(() -> new TableroExcepcion("Tarea no encontrada", HttpStatus.NOT_FOUND));

            if (tarea.getRecursos() != null && tarea.getRecursos().size() >= 4) {
                throw new TableroExcepcion("Límite de 4 recursos alcanzado para esta tarea",
                        HttpStatus.UNPROCESSABLE_ENTITY);
            }

            entity.setTareaAsociada(tarea);
        }

        return repositorio.save(entity);
    }

    @Override
    public void eliminarEntregable(UUID id) {
        EntregablesEntity entity = repositorio.findById(id)
                .orElseThrow(() -> new TableroExcepcion("Entregable no encontrado", HttpStatus.NOT_FOUND));

        if (entity.getTipoEntregable() == TipoEntregable.ARCHIVO && entity.getRuta() != null) {
            try {
                Path file = Paths.get(entity.getRuta());
                Files.deleteIfExists(file);
            } catch (IOException e) {
                log.error("No se pudo eliminar el archivo: {}", e.getMessage());
            }
        }
        repositorio.delete(entity);
    }

    @Override
    public EntregablesEntity actualizarRecurso(UUID id, EntregableDtoEntrada entregableDto) {
        EntregablesEntity entity = repositorio.findById(id)
                .orElseThrow(() -> new TableroExcepcion("Entregable no encontrado", HttpStatus.NOT_FOUND));

        try {
            if (entregableDto.getTipoEntregable() != null) {
                TipoEntregable nuevoTipo = TipoEntregable.valueOf(entregableDto.getTipoEntregable().toUpperCase());

                // Si cambiamos de archivo a enlace o subimos nuevo archivo, borrar el anterior
                // si existe
                if ((entity.getTipoEntregable() == TipoEntregable.ARCHIVO) &&
                        (nuevoTipo == TipoEntregable.ENLACE
                                || (nuevoTipo == TipoEntregable.ARCHIVO && entregableDto.getArchivo() != null))) {
                    if (entity.getRuta() != null) {
                        Files.deleteIfExists(Paths.get(entity.getRuta()));
                        entity.setRuta(null);
                        entity.setNombreArchivo(null);
                    }
                }

                entity.setTipoEntregable(nuevoTipo);

                if (nuevoTipo == TipoEntregable.ARCHIVO && entregableDto.getArchivo() != null
                        && !entregableDto.getArchivo().isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + entregableDto.getArchivo().getOriginalFilename();
                    Files.copy(entregableDto.getArchivo().getInputStream(), this.rootLocation.resolve(fileName));
                    entity.setNombreArchivo(entregableDto.getArchivo().getOriginalFilename());
                    entity.setRuta(this.rootLocation.resolve(fileName).toString());
                    entity.setRecurso(fileName);
                } else if (nuevoTipo == TipoEntregable.ENLACE) {
                    if (entregableDto.getEnlace() != null) {
                        entity.setRecurso(entregableDto.getEnlace());
                    }
                }
            } else {
                // Si no se especifica tipo pero se manda enlace
                if (entity.getTipoEntregable() == TipoEntregable.ENLACE && entregableDto.getEnlace() != null) {
                    entity.setRecurso(entregableDto.getEnlace());
                }
            }

            if (entregableDto.getIdTarea() != null) {
                UUID tareaId;
                try {
                    tareaId = UUID.fromString(entregableDto.getIdTarea());
                } catch (IllegalArgumentException e) {
                    throw new TableroExcepcion("ID de tarea con formato inválido", HttpStatus.BAD_REQUEST);
                }
                if (tareasRepositorio.existsById(tareaId)) {
                    TareaEntity tarea = new TareaEntity();
                    tarea.setId(tareaId);
                    entity.setTareaAsociada(tarea);
                }
            }

            repositorio.save(entity);
            return entity;
        } catch (IOException e) {
            throw new TableroExcepcion("Error al actualizar el archivo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<EntregableDtoSalida> listarEntregables() {
        return mapper.toDtoList(repositorio.findAll());
    }
}
