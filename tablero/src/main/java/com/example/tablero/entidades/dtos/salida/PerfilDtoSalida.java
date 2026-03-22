package com.example.tablero.entidades.dtos.salida;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PerfilDtoSalida {
  private String nombre;
  private String correo;
  private String nickName;
}
