package com.example.tablero.entidades.dtos.entrada;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AprovacionDtoEntrada {
    @NotBlank(message = "El estado de aprobación es obligatorio")
    @Pattern(regexp = "^(APROBADO|CAMBIOS_SOLICITADOS|RECHAZADA)$", message = "El estado de aprobación debe ser APROBADO, CAMBIOS_SOLICITADOS o RECHAZADA")
    private String estadoAprovacion;
    private String comentario;
    @NotBlank(message = "El id de la tarea es obligatorio")
    private String idTarea;
}
