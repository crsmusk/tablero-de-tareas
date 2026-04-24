package com.example.tablero.entidades.dtos.entrada;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProyectoDtoEntrada {
    @NotBlank(message = "El nombre del proyecto es obligatorio")
    private String nombreProyecto;
    private String nombreCliente;
    @Email(message = "El formato del correo del cliente no es válido")
    private String correoCliente;
}
