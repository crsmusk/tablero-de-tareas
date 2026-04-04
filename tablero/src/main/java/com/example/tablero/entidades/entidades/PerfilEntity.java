package com.example.tablero.entidades.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Perfiles")
public class PerfilEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank
    @Email
    private String correo;
    @NotBlank
    private String contraseña;
    private String nombre;
    @Column(name = "nick_name")
    private String nickName;
    @OneToMany
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ProyectoEntity> proyectos;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "perfiles_roles", joinColumns = @JoinColumn(name = "perfil_id"), inverseJoinColumns = @JoinColumn(name = "rol_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RolEntity> roles = new HashSet<>();
}
