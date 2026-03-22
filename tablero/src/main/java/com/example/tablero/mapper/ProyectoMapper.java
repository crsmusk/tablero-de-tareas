package com.example.tablero.mapper;

import com.example.tablero.entidades.dtos.salida.ProyectoDtoSalida;
import com.example.tablero.entidades.dtos.salida.ProyectoResumidoDtoSalida;
import com.example.tablero.entidades.entidades.ProyectoEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProyectoMapper {
    private ProyectoResumidoDtoSalida proyectoResumidoM(ProyectoEntity proyecto) {
        ProyectoResumidoDtoSalida salida = ProyectoResumidoDtoSalida.builder()
                .id(proyecto.getId().toString())
                .titulo(proyecto.getNombreProyecto())
                .nombreCliente(
                        proyecto.getNombreCliente() != null ? proyecto.getNombreCliente() : "sin cliente establecido")
                .build();
        return salida;
    }

    public List<ProyectoResumidoDtoSalida> ProyectosResumidoM(List<ProyectoEntity> proyectos) {
        if (proyectos == null || proyectos.isEmpty()) {
            return List.of();
        }
        return proyectos.stream().map(this::proyectoResumidoM).toList();
    }

    public ProyectoDtoSalida proyectoM(ProyectoEntity proyecto, int tareasCompletadas, int tareasPendientes,
            int tareasEnProceso, int tareasEnRevision) {
        ProyectoDtoSalida salida = ProyectoDtoSalida.builder()
                .id(proyecto.getId().toString())
                .titulo(proyecto.getNombreProyecto())
                .nombreCliente(
                        proyecto.getNombreCliente() != null ? proyecto.getNombreCliente() : "sin cliente establecido")
                .acceso(proyecto.getAcceso())
                .correoCliente(
                        proyecto.getCorreoCliente() != null ? proyecto.getCorreoCliente() : "sin correo establecido")
                .tareasCompletadas(tareasCompletadas)
                .tareasEnProceso(tareasEnProceso)
                .tareasEnRevision(tareasEnRevision)
                .tareasPendientes(tareasPendientes)
                .build();
        return salida;
    }
}
