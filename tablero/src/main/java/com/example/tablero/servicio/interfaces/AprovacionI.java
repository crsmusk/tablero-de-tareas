package com.example.tablero.servicio.interfaces;

import com.example.tablero.entidades.dtos.entrada.AprovacionDtoEntrada;
import com.example.tablero.entidades.dtos.salida.AprovacionesDtoSalida;

import java.util.List;
import java.util.UUID;

public interface AprovacionI {
    void guardarVeredicto(AprovacionDtoEntrada dto);

    List<AprovacionesDtoSalida> buscarPorTarea(UUID tareaId);
}
