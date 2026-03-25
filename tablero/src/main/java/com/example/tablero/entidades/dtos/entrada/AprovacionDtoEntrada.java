package com.example.tablero.entidades.dtos.entrada;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AprovacionDtoEntrada {
    @NotBlank(message = "El estado de aprobación es obligatorio")
    @Pattern(regexp = "^(APROVADO|CAMBIOS_SOLICITADOS|RECHAZADA)$", message = "El estado de aprobación debe ser APROVADO, CAMBIOS_SOLICITADOS o RECHAZADA")
    private String estadoAprovacion;
    private String comentario;
}
