package com.example.tablero.repositorio;

import com.example.tablero.entidades.entidades.ProyectoEntity;
import com.example.tablero.entidades.entidades.TareaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProyectoRepositorio extends JpaRepository<ProyectoEntity, UUID> {

    List<ProyectoEntity> findByPerfilAsociado_Correo(String correo);

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

    @Query("SELECT DISTINCT t FROM ProyectoEntity t WHERE t.perfilAsociado.id = :usuarioId")
    List<ProyectoEntity> findByUsuarioId(@Param("usuarioId") UUID usuarioId);

}
