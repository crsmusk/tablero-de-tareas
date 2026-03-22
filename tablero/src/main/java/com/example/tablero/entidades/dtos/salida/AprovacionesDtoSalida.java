package com.example.tablero.entidades.dtos.salida;

import com.example.tablero.entidades.entidades.enums.EstadoAprovado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AprovacionesDtoSalida {
    private String id;
    private String estadoAprovacion;
    private String comentario;
    private LocalDate fecha;
}
