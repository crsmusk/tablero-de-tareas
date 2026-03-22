package com.example.tablero.mapper;

import com.example.tablero.entidades.dtos.salida.PerfilDtoSalida;
import com.example.tablero.entidades.entidades.PerfilEntity;
import org.springframework.stereotype.Component;

@Component
public class PerfilMapper {
    public PerfilDtoSalida PerfilM(PerfilEntity perfil){
        return PerfilDtoSalida.builder()
                .correo(perfil.getCorreo())
                .nombre(perfil.getNombre())
                .nickName(perfil.getNickName())
                .build();
    }
}
