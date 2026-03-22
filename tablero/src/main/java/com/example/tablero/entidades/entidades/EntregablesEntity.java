package com.example.tablero.entidades.entidades;

import com.example.tablero.entidades.entidades.enums.TipoEntregable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Entregables")
public class EntregablesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String Recurso;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_entregrable")
    private TipoEntregable tipoEntregable;
    private String nombreArchivo;
    @Column(length = 1000)
    private String ruta;
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TareaEntity tareaAsociada;
}
