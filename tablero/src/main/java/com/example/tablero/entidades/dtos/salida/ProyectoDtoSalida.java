package com.example.tablero.entidades.dtos.salida;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProyectoDtoSalida {
    private String id;
    private String titulo;
    private String nombreCliente;
    private String correoCliente;
    private String acceso;
    private int tareasCompletadas;
    private  int tareasPendientes;
    private int tareasEnProceso;
    private int tareasEnRevision;
}
