package com.example.tablero.entidades.entidades;

import com.example.tablero.entidades.entidades.enums.EstadosProyecto;
import jakarta.persistence.*;
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
@Table(name = "Proyectos")
public class ProyectoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "nombre_proyecto")
    private String nombreProyecto;
    @Column(name = "nombre_cliente")
    private String nombreCliente;
    @Column(name = "correo_cliente")
    private String correoCliente;
    private String acceso;
    @Enumerated(EnumType.STRING)
    private EstadosProyecto estado = EstadosProyecto.ACTIVO;
    @OneToMany(mappedBy = "proyectoAsociado", orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<TareaEntity> tareas;
    @ManyToOne
    private PerfilEntity perfilAsociado;
}
