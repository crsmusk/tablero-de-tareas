package com.example.tablero.entidades.entidades;

import com.example.tablero.entidades.entidades.enums.RolNombre;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Entidad que representa los roles que pueden asignarse a los usuarios
 * (perfiles) en el sistema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_nombre", nullable = false, unique = true, length = 50)
    private RolNombre rolNombre;
}
