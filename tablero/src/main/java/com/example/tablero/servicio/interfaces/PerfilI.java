package com.example.tablero.servicio.interfaces;

import com.example.tablero.entidades.dtos.entrada.PerfilDtoEntrada;
import com.example.tablero.entidades.dtos.salida.PerfilDtoSalida;

import java.util.UUID;

public interface PerfilI {
    public void guardarPerfil(PerfilDtoEntrada perfilDto);
    public PerfilDtoSalida buscarPerfil(UUID id);
    public void eliminarPerfil(UUID id);
    public void actualizarPerfil(UUID id,PerfilDtoEntrada perfilDto);
}
