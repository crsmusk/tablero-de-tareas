package com.example.tablero.entidades.entidades;

import com.example.tablero.entidades.entidades.enums.EstadosTarea;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Tareas")
public class TareaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank
    private String titulo;
    private String descripcion;
    @OneToMany(mappedBy = "tareaAsociada", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Size(max = 4, message = "No se pueden tener más de 4 entregables")
    private List<EntregablesEntity> recursos;
    private int posicion;
    @Enumerated(EnumType.STRING)
    private EstadosTarea estado = EstadosTarea.PENDIENTE;
    @Column(name = "cliente_accion")
    private String clienteAccion;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "tarea_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<AprovacionEntity> aprovaciones;
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ProyectoEntity proyectoAsociado;
}
