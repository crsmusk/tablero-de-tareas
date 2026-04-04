package com.example.tablero.repositorio;

import com.example.tablero.entidades.entidades.PerfilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerfilRepositorio extends JpaRepository<PerfilEntity, UUID> {
    Optional<PerfilEntity> findByCorreo(String correo);
}
