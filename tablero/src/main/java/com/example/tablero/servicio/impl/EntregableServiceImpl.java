package com.example.tablero.servicio.impl;

import com.example.tablero.entidades.dtos.entrada.EntregableDtoEntrada;
import com.example.tablero.entidades.dtos.salida.EntregableDtoSalida;
import com.example.tablero.entidades.entidades.EntregablesEntity;
import com.example.tablero.entidades.entidades.TareaEntity;
import com.example.tablero.entidades.entidades.enums.TipoEntregable;
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import com.example.tablero.mapper.EntregableMapper;
import com.example.tablero.repositorio.EntregablesRepositorio;
import com.example.tablero.repositorio.TareaRepositorio;
import com.example.tablero.servicio.interfaces.AlmacenamientoI;
import com.example.tablero.servicio.interfaces.EntregableI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class EntregableServiceImpl implements EntregableI {

    private EntregablesRepositorio repositorio;
    private EntregableMapper mapper;
    private TareaRepositorio tareasRepositorio;
    private AlmacenamientoI almacenamientoService;

    public EntregableServiceImpl(EntregablesRepositorio repositorio, EntregableMapper mapper,
            TareaRepositorio tareasRepositorio,
            AlmacenamientoI almacenamientoService) {
        this.repositorio = repositorio;
        this.mapper = mapper;
        this.tareasRepositorio = tareasRepositorio;
        this.almacenamientoService = almacenamientoService;
    }

    @Override
    @Transactional
    public void guardarEntregable(EntregableDtoEntrada entregableDto) {
        EntregablesEntity entity = new EntregablesEntity();

        TipoEntregable tipo = parsearTipo(entregableDto.getTipoEntregable());
        entity.setTipoEntregable(tipo);

        if (tipo == TipoEntregable.ARCHIVO) {
            if (entregableDto.getArchivo() == null || entregableDto.getArchivo().isEmpty()) {
                throw new TableroExcepcion("Se requiere un archivo para este tipo de entregable",
                        HttpStatus.BAD_REQUEST);
            }
            String nombreUnico = almacenamientoService.guardarArchivo(entregableDto.getArchivo());
            entity.setNombreArchivo(entregableDto.getArchivo().getOriginalFilename());
            entity.setRuta("uploads/" + nombreUnico);
            entity.setRecurso(nombreUnico);
        } else if (tipo == TipoEntregable.ENLACE) {
            entity.setRecurso(entregableDto.getEnlace());
        }

        asociarTarea(entity, entregableDto.getIdTarea());
        repositorio.save(entity);
    }

    @Override
    @Transactional
    public void eliminarEntregable(UUID id) {
        EntregablesEntity entity = repositorio.findById(id)
                .orElseThrow(() -> new TableroExcepcion("Entregable no encontrado", HttpStatus.NOT_FOUND));

        if (entity.getTipoEntregable() == TipoEntregable.ARCHIVO) {
            almacenamientoService.eliminarArchivo(entity.getRuta());
        }

        repositorio.delete(entity);
    }

    @Override
    @Transactional
    public void actualizarRecurso(UUID id, EntregableDtoEntrada entregableDto) {
        EntregablesEntity entity = repositorio.findById(id)
                .orElseThrow(() -> new TableroExcepcion("Entregable no encontrado", HttpStatus.NOT_FOUND));

        if (entregableDto.getTipoEntregable() != null) {
            TipoEntregable nuevoTipo = parsearTipo(entregableDto.getTipoEntregable());

            // Si el anterior era ARCHIVO y se cambia de tipo o se sube uno nuevo, borrar el
            // viejo
            if (entity.getTipoEntregable() == TipoEntregable.ARCHIVO &&
                    (nuevoTipo == TipoEntregable.ENLACE
                            || (nuevoTipo == TipoEntregable.ARCHIVO && entregableDto.getArchivo() != null))) {
                almacenamientoService.eliminarArchivo(entity.getRuta());
                entity.setRuta(null);
                entity.setNombreArchivo(null);
            }

            entity.setTipoEntregable(nuevoTipo);

            if (nuevoTipo == TipoEntregable.ARCHIVO && entregableDto.getArchivo() != null
                    && !entregableDto.getArchivo().isEmpty()) {
                String nombreUnico = almacenamientoService.guardarArchivo(entregableDto.getArchivo());
                entity.setNombreArchivo(entregableDto.getArchivo().getOriginalFilename());
                entity.setRuta("uploads/" + nombreUnico);
                entity.setRecurso(nombreUnico);
            } else if (nuevoTipo == TipoEntregable.ENLACE && entregableDto.getEnlace() != null) {
                entity.setRecurso(entregableDto.getEnlace());
            }
        } else {
            if (entity.getTipoEntregable() == TipoEntregable.ENLACE && entregableDto.getEnlace() != null) {
                entity.setRecurso(entregableDto.getEnlace());
            }
        }

        asociarTarea(entity, entregableDto.getIdTarea());
        repositorio.save(entity);
    }

    @Override
    public List<EntregableDtoSalida> listarEntregables(String idTarea) {
        return mapper.toDtoList(repositorio.findByTareaAsociadaId(UUID.fromString(idTarea)));
    }

    private TipoEntregable parsearTipo(String tipoStr) {
        try {
            return TipoEntregable.valueOf(tipoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TableroExcepcion("Tipo de entregable inválido: " + tipoStr, HttpStatus.BAD_REQUEST);
        }
    }

    private void asociarTarea(EntregablesEntity entity, String idTarea) {
        if (idTarea == null) {
            return;
        }
        UUID tareaId;
        try {
            tareaId = UUID.fromString(idTarea);
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
}
