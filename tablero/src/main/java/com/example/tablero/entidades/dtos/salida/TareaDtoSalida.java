package com.example.tablero.entidades.dtos.salida;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TareaDtoSalida {
    private String id;
    private List<EntregableDtoSalida> entregables;
    private String clienteAccion;
    private String titulo;
    private String estado;
    private String descripcion;
    private List<AprovacionesDtoSalida> aprovaciones;
}
