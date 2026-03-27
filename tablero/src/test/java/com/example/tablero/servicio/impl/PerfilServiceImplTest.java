package com.example.tablero.servicio.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.example.tablero.entidades.dtos.entrada.PerfilDtoEntrada;
import com.example.tablero.entidades.dtos.salida.PerfilDtoSalida;
import com.example.tablero.entidades.entidades.PerfilEntity;
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import com.example.tablero.mapper.PerfilMapper;
import com.example.tablero.repositorio.PerfilRepositorio;

@ExtendWith(MockitoExtension.class)
class PerfilServiceImplTest {

    @Mock
    private PerfilRepositorio repositorio;

    @Mock
    private PerfilMapper mapper;

    @InjectMocks
    private PerfilServiceImpl service;

    @Test
    void debeGuardarPerfilExitosamente() {
        // Given
        PerfilDtoEntrada dto = PerfilDtoEntrada.builder()
                .nombre("Juan Perez")
                .correo("juan@example.com")
                .contraseña("123456")
                .nickName("juanp")
                .build();

        // When
        service.guardarPerfil(dto);

        // Then
        verify(repositorio).save(any(PerfilEntity.class));
    }

    @Test
    void debeRetornarPerfilCuandoExiste() {
        // Given
        UUID id = UUID.randomUUID();
        PerfilEntity entity = new PerfilEntity();
        entity.setId(id);
        entity.setNombre("Juan");

        PerfilDtoSalida dtoSalida = PerfilDtoSalida.builder().nombre("Juan").build();

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.PerfilM(entity)).thenReturn(dtoSalida);

        // When
        PerfilDtoSalida resultado = service.buscarPerfil(id);

        // Then
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        verify(repositorio).findById(id);
        verify(mapper).PerfilM(entity);
    }

    @Test
    void debeEliminarPerfilExitosamente() {
        // Given
        UUID id = UUID.randomUUID();
        PerfilEntity entity = new PerfilEntity();
        entity.setId(id);

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));

        // When
        service.eliminarPerfil(id);

        // Then
        verify(repositorio).delete(entity);
    }

    @Test
    void debeLanzarExcepcionCuandoPerfilNoExisteAlEliminar() {
        // Given
        UUID id = UUID.randomUUID();
        when(repositorio.findById(id)).thenReturn(Optional.empty());

        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.eliminarPerfil(id));
        assertEquals(HttpStatus.NOT_FOUND, ex.getEstadoHttp());
        assertTrue(ex.getMessage().contains("No se encontró el perfil"));
    }

    @Test
    void debeActualizarCamposProporcionados() {
        // Given
        UUID id = UUID.randomUUID();
        PerfilEntity entity = new PerfilEntity();
        entity.setId(id);
        entity.setNombre("Original");
        entity.setCorreo("original@example.com");

        PerfilDtoEntrada dto = PerfilDtoEntrada.builder()
                .nombre("Nuevo Nombre")
                .build();

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));

        // When
        service.actualizarPerfil(id, dto);

        // Then
        assertEquals("Nuevo Nombre", entity.getNombre());
        assertEquals("original@example.com", entity.getCorreo()); // No debe cambiar
        verify(repositorio).save(entity);
    }

    @Test
    void debeLanzarExcepcionCuandoPerfilNoExisteAlActualizar() {
        // Given
        UUID id = UUID.randomUUID();
        PerfilDtoEntrada dto = PerfilDtoEntrada.builder().nombre("Nuevo").build();
        when(repositorio.findById(id)).thenReturn(Optional.empty());

        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.actualizarPerfil(id, dto));
        assertEquals(HttpStatus.NOT_FOUND, ex.getEstadoHttp());
    }
}
