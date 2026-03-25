package com.example.tablero.entidades.dtos.entrada;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TareaDtoEntrada {
    @NotBlank(message = "El titulo no puede estar vacio")
    private String titulo;
    private String descripcion;
    private String clienteAccion;
    @Valid
    @Size(max = 4, message = "Máximo 4 entregables permitidos")
    private java.util.List<EntregableDtoEntrada> entregables;
    @Pattern(regexp = "^(PENDIENTE|EN_PROGRESO|EN_REVISION|COMPLETADO)$", message = "El estado de la tarea no es válido")
    private String estado;
    @NotBlank(message = "El id del proyecto es obligatorio")
    private String idProyectoAsociado;
    private int posicion;
}
