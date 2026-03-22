package com.example.tablero.entidades.dtos.entrada;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProyectoDtoEntrada {
    private String nombreProyecto;
    private String nombreCliente;
    private String correoCliente;
    private String idPerfil;
}
