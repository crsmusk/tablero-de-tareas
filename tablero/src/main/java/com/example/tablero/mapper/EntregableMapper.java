package com.example.tablero.mapper;

import com.example.tablero.entidades.dtos.salida.EntregableDtoSalida;
import com.example.tablero.entidades.entidades.EntregablesEntity;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class EntregableMapper {

    private EntregableDtoSalida toDto(EntregablesEntity entity) {
        if (entity == null) {
            return null;
        }
        return EntregableDtoSalida.builder()
                .id(entity.getId())
                .recurso(entity.getRecurso()) // Entity uses Recurso (uppercase R)
                .build();
    }

    public List<EntregableDtoSalida> toDtoList(List<EntregablesEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream().map(this::toDto).toList();
    }
}
