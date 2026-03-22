package com.example.tablero.repositorio;

import com.example.tablero.entidades.entidades.EntregablesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EntregablesRepositorio extends JpaRepository<EntregablesEntity, UUID> {
}
