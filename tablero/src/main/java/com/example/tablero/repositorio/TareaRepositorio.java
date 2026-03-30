package com.example.tablero.repositorio;

import com.example.tablero.entidades.entidades.TareaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TareaRepositorio extends JpaRepository<TareaEntity, UUID> {
    @Query("SELECT DISTINCT t FROM TareaEntity t LEFT JOIN FETCH t.recursos WHERE UPPER(t.titulo) LIKE UPPER(CONCAT('%', :titulo, '%'))")
    List<TareaEntity> findByTituloContainingIgnoreCase(@Param("titulo") String titulo);

    @Query("SELECT DISTINCT t FROM TareaEntity t LEFT JOIN FETCH t.recursos")
    List<TareaEntity> findAll();
}
