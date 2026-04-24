package com.example.tablero.servicio.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.tablero.entidades.dtos.entrada.ProyectoDtoEntrada;
import com.example.tablero.entidades.dtos.salida.ProyectoDtoSalida;
import com.example.tablero.entidades.dtos.salida.ProyectoResumidoDtoSalida;
import com.example.tablero.entidades.entidades.ProyectoEntity;
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import com.example.tablero.mapper.ProyectoMapper;
import com.example.tablero.repositorio.PerfilRepositorio;
import com.example.tablero.repositorio.ProyectoRepositorio;

@ExtendWith(MockitoExtension.class)
class ProyectoServiceImplTest {

    @Mock
    private ProyectoRepositorio repositorio;

    @Mock
    private PerfilRepositorio perfilRepositorio;

    @Mock
    private ProyectoMapper mapper;

    @InjectMocks
    private ProyectoServiceImpl service;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void debeGuardarProyectoExitosamente() {
        // Given
        UUID perfilId = UUID.randomUUID();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("usuario@example.com", perfilId, List.of()));

        ProyectoDtoEntrada dto = ProyectoDtoEntrada.builder()
                .nombreProyecto("Nuevo Proyecto")
                .build();

        when(perfilRepositorio.existsById(perfilId)).thenReturn(true);

        // When
        String resultado = service.guardarProyecto(dto);

        // Then
        assertEquals("proximamente se agregara esto", resultado);
        verify(repositorio).save(any(ProyectoEntity.class));
    }

    @Test
    void debeLanzarExcepcionCuandoPerfilNoExiste() {
        // Given
        UUID perfilId = UUID.randomUUID();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("usuario@example.com", perfilId, List.of()));

        ProyectoDtoEntrada dto = ProyectoDtoEntrada.builder()
                .build();

        when(perfilRepositorio.existsById(perfilId)).thenReturn(false);

        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.guardarProyecto(dto));
        assertEquals(HttpStatus.NOT_FOUND, ex.getEstadoHttp());
        assertTrue(ex.getMessage().contains("no se encontro al usuario"));
    }

    @Test
    void debeListarProyectosDelUsuarioAutenticado() {
        // Given
        UUID userId = UUID.randomUUID();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("usuario@example.com", userId, List.of()));

        List<ProyectoEntity> entities = List.of(new ProyectoEntity());
        List<ProyectoResumidoDtoSalida> dtos = List.of(ProyectoResumidoDtoSalida.builder().build());

        when(repositorio.findByUsuarioId(userId)).thenReturn(entities);
        when(mapper.ProyectosResumidoM(entities)).thenReturn(dtos);

        // When
        List<ProyectoResumidoDtoSalida> resultado = service.listaProyectos();

        // Then
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(repositorio).findByUsuarioId(userId);
        verify(mapper).ProyectosResumidoM(entities);
    }

    @Test
    void debeRetornarProyectoCuandoExiste() {
        // Given
        UUID id = UUID.randomUUID();
        ProyectoEntity entity = new ProyectoEntity();
        entity.setId(id);

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));
        when(repositorio.contarTareasCompletadas(id)).thenReturn(1);
        when(repositorio.contarTareasPendientes(id)).thenReturn(2);
        when(repositorio.contarTareasEnProgreso(id)).thenReturn(3);
        when(repositorio.contarTareasEnRevision(id)).thenReturn(4);

        ProyectoDtoSalida dtoSalida = ProyectoDtoSalida.builder().id(id.toString()).build();
        when(mapper.proyectoM(entity, 1, 2, 3, 4)).thenReturn(dtoSalida);

        // When
        ProyectoDtoSalida resultado = service.buscarProyecto(id);

        // Then
        assertNotNull(resultado);
        assertEquals(id.toString(), resultado.getId());
        verify(repositorio).findById(id);
        verify(repositorio).contarTareasCompletadas(id);
        verify(repositorio).contarTareasPendientes(id);
        verify(repositorio).contarTareasEnProgreso(id);
        verify(repositorio).contarTareasEnRevision(id);
        verify(mapper).proyectoM(entity, 1, 2, 3, 4);
    }

    @Test
    void debeLanzarExcepcionCuandoNoExisteAlBuscar() {
        // Given
        UUID id = UUID.randomUUID();
        when(repositorio.findById(id)).thenReturn(Optional.empty());

        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.buscarProyecto(id));
        assertEquals(HttpStatus.NOT_FOUND, ex.getEstadoHttp());
    }

    @Test
    void debeActualizarCamposProporcionados() {
        // Given
        UUID id = UUID.randomUUID();
        ProyectoEntity entity = new ProyectoEntity();
        entity.setId(id);
        entity.setNombreProyecto("Original");

        ProyectoDtoEntrada dto = ProyectoDtoEntrada.builder()
                .nombreProyecto("Nuevo")
                .build();

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));

        // When
        service.actualizarProyecto(id, dto);

        // Then
        assertEquals("Nuevo", entity.getNombreProyecto());
        verify(repositorio).save(entity);
    }

    @Test
    void debeEliminarProyectoExitosamente() {
        // Given
        UUID id = UUID.randomUUID();
        ProyectoEntity entity = new ProyectoEntity();
        entity.setId(id);

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));

        // When
        service.eliminarProyecto(id);

        // Then
        verify(repositorio).delete(entity);
    }
}
