package com.example.tablero.repositorio;

import com.example.tablero.entidades.entidades.ProyectoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProyectoRepositorio extends JpaRepository<ProyectoEntity, UUID> {

    @Query("""
    SELECT COUNT(t)
    FROM ProyectoEntity p
    JOIN p.tareas t
    WHERE p.id = :proyectoId
    AND t.estado = EstadosTarea.PENDIENTE
""")
    int contarTareasPendientes(UUID proyectoId);


    @Query("""
    SELECT COUNT(t)
    FROM ProyectoEntity p
    JOIN p.tareas t
    WHERE p.id = :proyectoId
    AND t.estado = EstadosTarea.EN_PROGRESO
""")
    int contarTareasEnProgreso(UUID proyectoId);

    @Query("""
    SELECT COUNT(t)
    FROM ProyectoEntity p
    JOIN p.tareas t
    WHERE p.id = :proyectoId
    AND t.estado = EstadosTarea.EN_REVISION
""")
    int contarTareasEnRevision(UUID proyectoId);

    @Query("""
    SELECT COUNT(t)
    FROM ProyectoEntity p
    JOIN p.tareas t
    WHERE p.id = :proyectoId
    AND t.estado = EstadosTarea.COMPLETADO
""")
    int contarTareasCompletadas(UUID proyectoId);
}
