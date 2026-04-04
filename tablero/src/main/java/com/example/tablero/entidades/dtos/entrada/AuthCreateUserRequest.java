package com.example.tablero.entidades.dtos.entrada;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record AuthCreateUserRequest(
                @NotBlank(message = "El correo es obligatorio") @Email(message = "Debe proporcionar un correo electrónico válido") String correo,

                @NotBlank(message = "La contraseña es obligatoria") String contraseña,

                @NotBlank(message = "El nombre es obligatorio") String nombre,

                String nickName,

                @Valid AuthCreateRoleRequest roleRequest) {

        public record AuthCreateRoleRequest(
                        @NotEmpty(message = "Debe asignar al menos un rol al usuario") List<String> roles) {
        }
}
