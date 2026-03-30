package com.example.tablero.mapper;

import com.example.tablero.entidades.dtos.salida.AprovacionesDtoSalida;
import com.example.tablero.entidades.entidades.AprovacionEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AprovacionMapper {

    public AprovacionesDtoSalida aprovacionM(AprovacionEntity entity) {
        if (entity == null)
            return null;
        return AprovacionesDtoSalida.builder()
                .id(entity.getId().toString())
                .estadoAprovacion(entity.getEstadoAprovacion().name())
                .comentario(entity.getComentario())
                .fecha(entity.getFecha())
                .build();
    }

    public List<AprovacionesDtoSalida> aprovacionesM(List<AprovacionEntity> entities) {
        if (entities == null)
            return List.of();
        return entities.stream().map(this::aprovacionM).collect(Collectors.toList());
    }
}
