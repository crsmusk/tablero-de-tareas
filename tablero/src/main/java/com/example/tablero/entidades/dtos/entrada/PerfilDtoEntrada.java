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
public class PerfilDtoEntrada {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es válido")
    private String correo;
    @NotBlank(message = "La contraseña es obligatoria")
    private String contraseña;
    @NotBlank(message = "El apodo es obligatorio")
    private String nickName;
}
