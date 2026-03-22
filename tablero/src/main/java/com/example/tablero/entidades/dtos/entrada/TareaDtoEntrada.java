package com.example.tablero.entidades.dtos.entrada;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(max = 4, message = "Máximo 4 entregables permitidos")
    private java.util.List<EntregableDtoEntrada> entregables;
    private String estado;
    @NotBlank(message = "El id del proyecto es obligatorio")
    private String idProyectoAsociado;
    private int posicion;
}
