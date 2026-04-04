package com.example.tablero.repositorio;

import com.example.tablero.entidades.entidades.RolEntity;
import com.example.tablero.entidades.entidades.enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolRepositorio extends JpaRepository<RolEntity, UUID> {
    Optional<RolEntity> findByRolNombre(RolNombre rolNombre);
}
