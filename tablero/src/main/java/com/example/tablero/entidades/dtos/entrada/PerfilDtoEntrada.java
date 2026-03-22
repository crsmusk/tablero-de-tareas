package com.example.tablero.entidades.dtos.entrada;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PerfilDtoEntrada {
    private String nombre;
    private String correo;
    private String contraseña;
    private String nickName;
}
