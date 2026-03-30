package com.example.tablero.servicio.impl;

import com.example.tablero.entidades.dtos.entrada.AprovacionDtoEntrada;
import com.example.tablero.entidades.dtos.salida.AprovacionesDtoSalida;
import com.example.tablero.entidades.dtos.salida.AprovacionesDtoSalida;
import com.example.tablero.entidades.entidades.TareaEntity;
import com.example.tablero.entidades.entidades.enums.EstadoAprovado;
import com.example.tablero.entidades.entidades.enums.EstadosTarea;
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import com.example.tablero.mapper.AprovacionMapper;
import com.example.tablero.repositorio.AprovacionRepositorio;
import com.example.tablero.repositorio.TareaRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AprovacionServiceImplTest {

    @Mock
    private AprovacionRepositorio aprovacionRepositorio;
    @Mock
    private TareaRepositorio tareaRepositorio;
    @Mock
    private AprovacionMapper aprovacionMapper;

    @InjectMocks
    private AprovacionServiceImpl service;

    private TareaEntity tarea;
    private UUID tareaId;

    @BeforeEach
    void setUp() {
        tareaId = UUID.randomUUID();
        tarea = new TareaEntity();
        tarea.setId(tareaId);
        tarea.setEstado(EstadosTarea.EN_REVISION);
        tarea.setAprovaciones(new ArrayList<>());
    }

    @Test
    void debeGuardarVeredictoExitosamente() {
        // Given
        AprovacionDtoEntrada dto = AprovacionDtoEntrada.builder()
                .idTarea(tareaId.toString())
                .estadoAprovacion("APROVADO")
                .comentario("Excelente trabajo")
                .build();

        when(tareaRepositorio.findById(tareaId)).thenReturn(Optional.of(tarea));

        // When
        service.guardarVeredicto(dto);

        // Then
        verify(tareaRepositorio).save(tarea);
        assertEquals(1, tarea.getAprovaciones().size());
        assertEquals(EstadoAprovado.APROVADO, tarea.getAprovaciones().getFirst().getEstadoAprovacion());
        assertEquals("Excelente trabajo", tarea.getAprovaciones().getFirst().getComentario());
    }

    @Test
    void debeLanzarExcepcionCuandoTareaNoExiste() {
        // Given
        UUID idInexistente = UUID.randomUUID();
        AprovacionDtoEntrada dto = AprovacionDtoEntrada.builder()
                .idTarea(idInexistente.toString())
                .build();

        when(tareaRepositorio.findById(idInexistente)).thenReturn(Optional.empty());

        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.guardarVeredicto(dto));
        assertEquals(HttpStatus.NOT_FOUND, ex.getEstadoHttp());
        assertTrue(ex.getMessage().contains("No se encontró la tarea"));
    }

    @Test
    void debeLanzarExcepcionCuandoTareaNoEstaEnRevision() {
        // Given
        tarea.setEstado(EstadosTarea.EN_PROGRESO);
        AprovacionDtoEntrada dto = AprovacionDtoEntrada.builder()
                .idTarea(tareaId.toString())
                .build();

        when(tareaRepositorio.findById(tareaId)).thenReturn(Optional.of(tarea));

        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.guardarVeredicto(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getEstadoHttp());
        assertTrue(ex.getMessage().contains("La tarea no está en estado EN_REVISION"));
    }

    @Test
    void debeBuscarAprovacionesPorTarea() {
        // Given
        when(tareaRepositorio.findById(tareaId)).thenReturn(Optional.of(tarea));
        when(aprovacionMapper.aprovacionesM(any())).thenReturn(List.of(new AprovacionesDtoSalida()));

        // When
        List<AprovacionesDtoSalida> resultado = service.buscarPorTarea(tareaId);

        // Then
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(tareaRepositorio).findById(tareaId);
    }
}
