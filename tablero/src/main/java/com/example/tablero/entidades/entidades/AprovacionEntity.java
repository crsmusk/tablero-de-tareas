package com.example.tablero.entidades.entidades;

import com.example.tablero.entidades.entidades.enums.EstadoAprovado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Aprovaciones")
public class AprovacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_aprovacion")
    private EstadoAprovado estadoAprovacion;
    private String comentario;
    private LocalDate fecha;
}
