package com.example.tablero.servicio.interfaces;

import com.example.tablero.entidades.dtos.entrada.EntregableDtoEntrada;
import com.example.tablero.entidades.dtos.salida.EntregableDtoSalida;

import java.util.List;
import java.util.UUID;

public interface EntregableI {
    void guardarEntregable(EntregableDtoEntrada entregableDto);

    void eliminarEntregable(UUID id);

    void actualizarRecurso(UUID id, EntregableDtoEntrada entregableDto);

    List<EntregableDtoSalida> listarEntregables(String idTarea);
}
