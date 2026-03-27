package com.example.tablero.servicio.interfaces;

import com.example.tablero.entidades.dtos.entrada.ProyectoDtoEntrada;
import com.example.tablero.entidades.dtos.salida.ProyectoDtoSalida;
import com.example.tablero.entidades.dtos.salida.ProyectoResumidoDtoSalida;

import java.util.List;
import java.util.UUID;

public interface ProyectoI {
    public String guardarProyecto(ProyectoDtoEntrada proyectoDto);

    public List<ProyectoResumidoDtoSalida> listaProyectos();

    public ProyectoDtoSalida buscarProyecto(UUID id);

    public void actualizarProyecto(UUID id, ProyectoDtoEntrada proyectoDto);

    public void eliminarProyecto(UUID id);
}
