package com.example.tablero.servicio.interfaces;

import com.example.tablero.entidades.dtos.entrada.EntregableDtoEntrada;
import com.example.tablero.entidades.dtos.salida.EntregableDtoSalida;
import com.example.tablero.entidades.entidades.EntregablesEntity;

import java.util.List;
import java.util.UUID;

public interface EntregableI {
    public EntregablesEntity guardarEntregable(EntregableDtoEntrada entregableDto);

    public void eliminarEntregable(UUID id);

    public EntregablesEntity actualizarRecurso(UUID id, EntregableDtoEntrada entregableDto);

    public List<EntregableDtoSalida> listarEntregables();
}
