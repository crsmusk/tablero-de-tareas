package com.example.tablero.servicio.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.example.tablero.entidades.dtos.entrada.EntregableDtoEntrada;
import com.example.tablero.entidades.dtos.entrada.TareaDtoEntrada;
import com.example.tablero.entidades.dtos.salida.TareaDtoSalida;
import com.example.tablero.entidades.entidades.TareaEntity;
import com.example.tablero.entidades.entidades.enums.EstadosTarea;
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import com.example.tablero.mapper.TareaMapper;
import com.example.tablero.repositorio.ProyectoRepositorio;
import com.example.tablero.repositorio.TareaRepositorio;

@ExtendWith(MockitoExtension.class)
class TareaServiceImplTest {

    @Mock
    private TareaRepositorio repositorio;

    @Mock
    private TareaMapper mapper;

    @Mock
    private ProyectoRepositorio repositorioProyecto;

    @Mock
    private EntregableServiceImpl entregableService;

    @InjectMocks
    private TareaServiceImpl service;

    @Test
    void debeGuardarTareaExitosamenteSinEntregables() {
        // Given
        UUID proyectoId = UUID.randomUUID();
        TareaDtoEntrada dto = TareaDtoEntrada.builder()
                .titulo("Nueva Tarea")
                .idProyectoAsociado(proyectoId.toString())
                .posicion(1)
                .build();

        when(repositorioProyecto.existsById(proyectoId)).thenReturn(true);

        // When
        service.guardarTarea(dto);

        // Then
        verify(repositorio).save(any(TareaEntity.class));
        verify(entregableService, never()).guardarEntregable(any());
    }

    @Test
    void debeGuardarTareaConEntregables() {
        // Given
        UUID proyectoId = UUID.randomUUID();
        UUID tareaId = UUID.randomUUID();
        EntregableDtoEntrada entregableDto = new EntregableDtoEntrada();
        TareaDtoEntrada dto = TareaDtoEntrada.builder()
                .titulo("Tarea con entregables")
                .idProyectoAsociado(proyectoId.toString())
                .entregables(Collections.singletonList(entregableDto))
                .build();

        when(repositorioProyecto.existsById(proyectoId)).thenReturn(true);

        doAnswer(invocation -> {
            TareaEntity t = invocation.getArgument(0);
            t.setId(tareaId);
            return t;
        }).when(repositorio).save(any(TareaEntity.class));

        // When
        service.guardarTarea(dto);

        // Then
        verify(repositorio).save(any(TareaEntity.class));
        assertEquals(tareaId.toString(), entregableDto.getIdTarea());
        verify(entregableService).guardarEntregable(entregableDto);
    }

    @Test
    void debeLanzarExcepcionCuandoProyectoNoExiste() {
        // Given
        UUID proyectoId = UUID.randomUUID();
        TareaDtoEntrada dto = TareaDtoEntrada.builder()
                .idProyectoAsociado(proyectoId.toString())
                .build();

        when(repositorioProyecto.existsById(proyectoId)).thenReturn(false);

        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.guardarTarea(dto));
        assertEquals(HttpStatus.NOT_FOUND, ex.getEstadoHttp());
        assertTrue(ex.getMessage().contains("No se encontró el proyecto"));
    }

    @Test
    void debeRetornarTareaCuandoExiste() {
        // Given
        UUID id = UUID.randomUUID();
        TareaEntity entity = new TareaEntity();
        entity.setId(id);

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.tareaM(entity)).thenReturn(TareaDtoSalida.builder().id(id.toString()).build());

        // When
        TareaDtoSalida resultado = service.buscarTareaPorId(id);

        // Then
        assertNotNull(resultado);
        assertEquals(id.toString(), resultado.getId());
    }

    @Test
    void debeIntercambiarPosicionesExitosamente() {
        // Given
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        TareaEntity tarea1 = new TareaEntity();
        tarea1.setId(id1);
        tarea1.setPosicion(10);

        TareaEntity tarea2 = new TareaEntity();
        tarea2.setId(id2);
        tarea2.setPosicion(20);

        when(repositorio.findById(id1)).thenReturn(Optional.of(tarea1));
        when(repositorio.findById(id2)).thenReturn(Optional.of(tarea2));

        // When
        service.cambiarOrden(id1, id2);

        // Then
        assertEquals(20, tarea1.getPosicion());
        assertEquals(10, tarea2.getPosicion());
        verify(repositorio).save(tarea1);
        verify(repositorio).save(tarea2);
    }

    @Test
    void debeBuscarPorTitulo() {
        // Given
        String titulo = "test";
        List<TareaEntity> entities = Collections.singletonList(new TareaEntity());
        when(repositorio.findByTituloContainingIgnoreCase(titulo)).thenReturn(entities);
        when(mapper.tareasM(entities)).thenReturn(Collections.singletonList(new TareaDtoSalida()));

        // When
        List<TareaDtoSalida> resultado = service.buscarTareaPorTitulo(titulo);

        // Then
        assertFalse(resultado.isEmpty());
        verify(repositorio).findByTituloContainingIgnoreCase(titulo);
    }

    @Test
    void debeListarTareas() {
        // Given
        UUID idProyecto = UUID.randomUUID();
        List<TareaEntity> entities = Collections.singletonList(new TareaEntity());
        when(repositorio.findByTareasProyecto(idProyecto)).thenReturn(entities);
        when(mapper.tareasM(entities)).thenReturn(Collections.singletonList(new TareaDtoSalida()));

        // When
        List<TareaDtoSalida> resultado = service.listarTarea(idProyecto);

        // Then
        assertFalse(resultado.isEmpty());
        verify(repositorio).findByTareasProyecto(idProyecto);
    }

    @Test
    void debeEliminarTareaExitosamente() {
        // Given
        UUID id = UUID.randomUUID();
        TareaEntity entity = new TareaEntity();
        when(repositorio.findById(id)).thenReturn(Optional.of(entity));

        // When
        service.eliminarTarea(id);

        // Then
        verify(repositorio).delete(entity);
    }

    @Test
    void debeActualizarCamposEnviados() {
        // Given
        UUID id = UUID.randomUUID();
        TareaEntity entity = new TareaEntity();
        entity.setId(id);
        entity.setTitulo("Original");

        TareaDtoEntrada dto = TareaDtoEntrada.builder()
                .titulo("Nuevo")
                .estado("COMPLETADO")
                .build();

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));

        // When
        service.actualizarTarea(id, dto);

        // Then
        assertEquals("Nuevo", entity.getTitulo());
        assertEquals(EstadosTarea.COMPLETADO, entity.getEstado());
        verify(repositorio).save(entity);
    }
}
