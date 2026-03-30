package com.example.tablero.servicio.impl;

import com.example.tablero.entidades.dtos.entrada.AprovacionDtoEntrada;
import com.example.tablero.entidades.dtos.salida.AprovacionesDtoSalida;
import com.example.tablero.entidades.entidades.AprovacionEntity;
import com.example.tablero.entidades.entidades.TareaEntity;
import com.example.tablero.entidades.entidades.enums.EstadoAprovado;
import com.example.tablero.entidades.entidades.enums.EstadosTarea;
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import com.example.tablero.mapper.AprovacionMapper;
import com.example.tablero.repositorio.AprovacionRepositorio;
import com.example.tablero.repositorio.TareaRepositorio;
import com.example.tablero.servicio.interfaces.AprovacionI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AprovacionServiceImpl implements AprovacionI {

    private final TareaRepositorio tareaRepositorio;
    private final AprovacionMapper aprovacionMapper;

    @Override
    @Transactional
    public void guardarVeredicto(AprovacionDtoEntrada dto) {
        TareaEntity tarea = tareaRepositorio.findById(UUID.fromString(dto.getIdTarea()))
                .orElseThrow(() -> new TableroExcepcion("No se encontró la tarea con ID: " + dto.getIdTarea(),
                        HttpStatus.NOT_FOUND));

        if (tarea.getEstado() != EstadosTarea.EN_REVISION) {
            throw new TableroExcepcion("La tarea no está en estado EN_REVISION. Estado actual: " + tarea.getEstado(),
                    HttpStatus.BAD_REQUEST);
        }

        AprovacionEntity aprovacion = new AprovacionEntity();
        aprovacion.setEstadoAprovacion(EstadoAprovado.valueOf(dto.getEstadoAprovacion()));
        aprovacion.setComentario(dto.getComentario());
        aprovacion.setFecha(LocalDate.now());

        tarea.getAprovaciones().add(aprovacion);

        tareaRepositorio.save(tarea);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AprovacionesDtoSalida> buscarPorTarea(UUID tareaId) {
        TareaEntity tarea = tareaRepositorio.findById(tareaId)
                .orElseThrow(
                        () -> new TableroExcepcion("No se encontró la tarea con ID: " + tareaId, HttpStatus.NOT_FOUND));

        return aprovacionMapper.aprovacionesM(tarea.getAprovaciones());
    }
}
