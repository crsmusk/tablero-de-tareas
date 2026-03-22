package com.example.tablero.entidades.dtos.salida;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProyectoResumidoDtoSalida {
    private String id;
    private String titulo;
    private String nombreCliente;
}
