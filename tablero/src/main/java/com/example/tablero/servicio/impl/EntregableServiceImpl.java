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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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
            throw new RuntimeException("No se pudo inicializar la carpeta de almacenamiento", e);
        }
    }

    @Override
    public EntregablesEntity guardarEntregable(EntregableDtoEntrada entregableDto) {
        EntregablesEntity entity = new EntregablesEntity();

        try {
            TipoEntregable tipo = TipoEntregable.valueOf(entregableDto.getTipoEntregable().toUpperCase());
            entity.setTipoEntregable(tipo);

            if (tipo == TipoEntregable.ARCHIVO && entregableDto.getArchivo() != null
                    && !entregableDto.getArchivo().isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + entregableDto.getArchivo().getOriginalFilename();
                Files.copy(entregableDto.getArchivo().getInputStream(), this.rootLocation.resolve(fileName));
                entity.setNombreArchivo(entregableDto.getArchivo().getOriginalFilename());
                entity.setRuta(this.rootLocation.resolve(fileName).toString());
                entity.setRecurso(fileName);
            } else if (tipo == TipoEntregable.ENLACE) {
                entity.setRecurso(entregableDto.getEnlace());
            }

            if (entregableDto.getIdTarea() != null
                    && tareasRepositorio.existsById(UUID.fromString(entregableDto.getIdTarea()))) {
                TareaEntity tarea = tareasRepositorio.findById(UUID.fromString(entregableDto.getIdTarea())).get();
                if (tarea.getRecursos() != null && tarea.getRecursos().size() >= 4) {
                    throw new RuntimeException("Límite de 4 recursos alcanzado para esta tarea");
                }
                entity.setTareaAsociada(tarea);
            }

            repositorio.save(entity);

            return entity;

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de entregable inválido: " + entregableDto.getTipoEntregable());
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }

    @Override
    public void eliminarEntregable(UUID id) {
        EntregablesEntity entity = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Entregable no encontrado"));

        if (entity.getTipoEntregable() == TipoEntregable.ARCHIVO && entity.getRuta() != null) {
            try {
                Path file = Paths.get(entity.getRuta());
                Files.deleteIfExists(file);
            } catch (IOException e) {
                System.err.println("No se pudo eliminar el archivo: " + e.getMessage());
            }
        }
        repositorio.delete(entity);
    }

    @Override
    public EntregablesEntity actualizarRecurso(UUID id, EntregableDtoEntrada entregableDto) {
        EntregablesEntity entity = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Entregable no encontrado"));

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

            if (entregableDto.getIdTarea() != null
                    && tareasRepositorio.existsById(UUID.fromString(entregableDto.getIdTarea()))) {
                TareaEntity tarea = new TareaEntity();
                tarea.setId(UUID.fromString(entregableDto.getIdTarea()));
                entity.setTareaAsociada(tarea);
            }

            repositorio.save(entity);
            return entity;
        } catch (IOException e) {
            throw new RuntimeException("Error al actualizar el archivo", e);
        }
    }

    @Override
    public List<EntregableDtoSalida> listarEntregables() {
        return mapper.toDtoList(repositorio.findAll());
    }
}
