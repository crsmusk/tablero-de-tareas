package com.example.tablero.entidades.dtos.salida;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDtoSalida {
    private LocalDateTime timestamp;
    private int estado;
    private String mensaje;
    private String detalles;
}
