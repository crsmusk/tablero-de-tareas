package com.example.tablero.mapper;

import com.example.tablero.entidades.dtos.salida.AprovacionesDtoSalida;
import com.example.tablero.entidades.dtos.salida.EntregableDtoSalida;
import com.example.tablero.entidades.dtos.salida.TareaDtoSalida;
import com.example.tablero.entidades.dtos.salida.TareaResumidaDtoSalida;
import com.example.tablero.entidades.entidades.AprovacionEntity;
import com.example.tablero.entidades.entidades.EntregablesEntity;
import com.example.tablero.entidades.entidades.TareaEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TareaMapper {

    public TareaDtoSalida tareaM(TareaEntity tarea) {
        return TareaDtoSalida.builder()
                .id(tarea.getId().toString())
                .estado(tarea.getEstado().toString())
                .descripcion(tarea.getDescripcion())
                .titulo(tarea.getTitulo())
                .clienteAccion(tarea.getClienteAccion())
                .entregables(
                        tarea.getRecursos() != null ? tarea.getRecursos().stream().map(this::toEntregableDto).toList()
                                : List.of())
                .aprovaciones(tarea.getAprovaciones() != null
                        ? tarea.getAprovaciones().stream().map(this::toAprovacionDto).toList()
                        : List.of())
                .build();
    }

    private EntregableDtoSalida toEntregableDto(EntregablesEntity entregable) {
        return EntregableDtoSalida.builder()
                .id(entregable.getId())
                .recurso(entregable.getRecurso())
                .build();
    }

    private AprovacionesDtoSalida toAprovacionDto(AprovacionEntity aprovacion) {
        return AprovacionesDtoSalida.builder()
                .id(aprovacion.getId().toString())
                .estadoAprovacion(aprovacion.getEstadoAprovacion().toString())
                .comentario(aprovacion.getComentario())
                .fecha(aprovacion.getFecha())
                .build();
    }

    public List<TareaDtoSalida> tareasM(List<TareaEntity> tareas) {
        if (tareas == null || tareas.isEmpty()) {
            return List.of();
        }
        return tareas.stream().map(this::tareaM).toList();
    }

    public TareaResumidaDtoSalida tareaResumidaM(TareaEntity tarea) {
        return TareaResumidaDtoSalida.builder()
                .id(tarea.getId().toString())
                .titulo(tarea.getTitulo())
                .estado(tarea.getEstado() != null ? tarea.getEstado().toString() : null)
                .build();
    }

    public List<TareaResumidaDtoSalida> tareasResumidasM(List<TareaEntity> tareas) {
        if (tareas == null || tareas.isEmpty()) {
            return List.of();
        }
        return tareas.stream().map(this::tareaResumidaM).toList();
    }
}
