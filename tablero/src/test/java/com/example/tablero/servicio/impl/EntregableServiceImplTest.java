package com.example.tablero.servicio.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import com.example.tablero.entidades.dtos.entrada.EntregableDtoEntrada;
import com.example.tablero.entidades.dtos.salida.EntregableDtoSalida;
import com.example.tablero.entidades.entidades.EntregablesEntity;
import com.example.tablero.entidades.entidades.TareaEntity;
import com.example.tablero.entidades.entidades.enums.TipoEntregable;
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import com.example.tablero.mapper.EntregableMapper;
import com.example.tablero.repositorio.EntregablesRepositorio;
import com.example.tablero.repositorio.TareaRepositorio;
import com.example.tablero.servicio.interfaces.AlmacenamientoI;

@ExtendWith(MockitoExtension.class)
class EntregableServiceImplTest {

    @Mock
    private EntregablesRepositorio repositorio;

    @Mock
    private EntregableMapper mapper;

    @Mock
    private TareaRepositorio tareasRepositorio;

    @Mock
    private AlmacenamientoI almacenamientoService;

    @InjectMocks
    private EntregableServiceImpl service;

    // --- Tests para guardarEntregable ---

    @Test
    void debeGuardarEntregableTipoEnlace() {
        // Given
        UUID tareaId = UUID.randomUUID();
        EntregableDtoEntrada dto = EntregableDtoEntrada.builder()
                .tipoEntregable("ENLACE")
                .enlace("http://google.com")
                .idTarea(tareaId.toString())
                .build();

        TareaEntity tarea = new TareaEntity();
        tarea.setId(tareaId);
        tarea.setRecursos(new ArrayList<>());

        when(tareasRepositorio.findById(tareaId)).thenReturn(Optional.of(tarea));

        // When
        service.guardarEntregable(dto);

        // Then
        verify(repositorio).save(any(EntregablesEntity.class));
        verify(almacenamientoService, never()).guardarArchivo(any());
    }

    @Test
    void debeGuardarEntregableTipoArchivo() {
        // Given
        MockMultipartFile archivo = new MockMultipartFile("archivo", "test.txt", "text/plain", "contenido".getBytes());
        EntregableDtoEntrada dto = EntregableDtoEntrada.builder()
                .tipoEntregable("ARCHIVO")
                .archivo(archivo)
                .build();

        when(almacenamientoService.guardarArchivo(archivo)).thenReturn("uuid_test.txt");

        // When
        service.guardarEntregable(dto);

        // Then
        verify(almacenamientoService).guardarArchivo(archivo);
        verify(repositorio).save(any(EntregablesEntity.class));
    }

    @Test
    void debeLanzarExcepcionCuandoTipoEsInvalido() {
        // Given
        EntregableDtoEntrada dto = EntregableDtoEntrada.builder()
                .tipoEntregable("INVALIDO")
                .build();

        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.guardarEntregable(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getEstadoHttp());
        assertTrue(ex.getMessage().contains("Tipo de entregable inválido"));
    }

    @Test
    void debeLanzarExcepcionCuandoArchivoEsNuloYTipoEsArchivo() {
        // Given
        EntregableDtoEntrada dto = EntregableDtoEntrada.builder()
                .tipoEntregable("ARCHIVO")
                .archivo(null)
                .build();

        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.guardarEntregable(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getEstadoHttp());
        assertEquals("Se requiere un archivo para este tipo de entregable", ex.getMessage());
    }

    @Test
    void debeLanzarExcepcionCuandoTareaNoExiste() {
        // Given
        UUID tareaId = UUID.randomUUID();
        EntregableDtoEntrada dto = EntregableDtoEntrada.builder()
                .tipoEntregable("ENLACE")
                .idTarea(tareaId.toString())
                .build();

        when(tareasRepositorio.findById(tareaId)).thenReturn(Optional.empty());

        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.guardarEntregable(dto));
        assertEquals(HttpStatus.NOT_FOUND, ex.getEstadoHttp());
        assertEquals("Tarea no encontrada", ex.getMessage());
    }

    @Test
    void debeLanzarExcepcionCuandoTareaTieneLimiteDeEntregables() {
        // Given
        UUID tareaId = UUID.randomUUID();
        EntregableDtoEntrada dto = EntregableDtoEntrada.builder()
                .tipoEntregable("ENLACE")
                .idTarea(tareaId.toString())
                .build();

        TareaEntity tarea = new TareaEntity();
        tarea.setId(tareaId);
        List<EntregablesEntity> recursos = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            recursos.add(new EntregablesEntity());
        }
        tarea.setRecursos(recursos);

        when(tareasRepositorio.findById(tareaId)).thenReturn(Optional.of(tarea));

        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.guardarEntregable(dto));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getEstadoHttp());
        assertEquals("Límite de 4 recursos alcanzado para esta tarea", ex.getMessage());
    }

    @Test
    void debeLanzarExcepcionCuandoIdTareaEsInvalido() {
        // Given
        EntregableDtoEntrada dto = EntregableDtoEntrada.builder()
                .tipoEntregable("ENLACE")
                .idTarea("uuid-invalido")
                .build();

        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.guardarEntregable(dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getEstadoHttp());
        assertEquals("ID de tarea con formato inválido", ex.getMessage());
    }

    // --- Tests para eliminarEntregable ---

    @Test
    void debeEliminarEntregableTipoEnlace() {
        // Given
        UUID id = UUID.randomUUID();
        EntregablesEntity entity = new EntregablesEntity();
        entity.setId(id);
        entity.setTipoEntregable(TipoEntregable.ENLACE);

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));

        // When
        service.eliminarEntregable(id);

        // Then
        verify(repositorio).delete(entity);
        verify(almacenamientoService, never()).eliminarArchivo(any());
    }

    @Test
    void debeEliminarEntregableTipoArchivoYDelegarBorrado() {
        // Given
        UUID id = UUID.randomUUID();
        EntregablesEntity entity = new EntregablesEntity();
        entity.setId(id);
        entity.setTipoEntregable(TipoEntregable.ARCHIVO);
        entity.setRuta("uploads/archivo.txt");

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));

        // When
        service.eliminarEntregable(id);

        // Then
        verify(almacenamientoService).eliminarArchivo("uploads/archivo.txt");
        verify(repositorio).delete(entity);
    }

    @Test
    void debeLanzarExcepcionCuandoEntregableNoExisteAlEliminar() {
        // Given
        UUID id = UUID.randomUUID();
        when(repositorio.findById(id)).thenReturn(Optional.empty());

        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.eliminarEntregable(id));
        assertEquals(HttpStatus.NOT_FOUND, ex.getEstadoHttp());
        assertEquals("Entregable no encontrado", ex.getMessage());
    }

    // --- Tests para actualizarRecurso ---

    @Test
    void debeActualizarDeArchivoAEnlaceYDelegarBorrado() {
        // Given
        UUID id = UUID.randomUUID();
        EntregablesEntity entity = new EntregablesEntity();
        entity.setId(id);
        entity.setTipoEntregable(TipoEntregable.ARCHIVO);
        entity.setRuta("uploads/old.txt");

        EntregableDtoEntrada dto = EntregableDtoEntrada.builder()
                .tipoEntregable("ENLACE")
                .enlace("http://newlink.com")
                .build();

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));

        // When
        service.actualizarRecurso(id, dto);

        // Then
        verify(almacenamientoService).eliminarArchivo("uploads/old.txt");
        assertEquals(TipoEntregable.ENLACE, entity.getTipoEntregable());
        assertEquals("http://newlink.com", entity.getRecurso());
        verify(repositorio).save(entity);
    }

    @Test
    void debeActualizarArchivoYDelegarBorradoDelAnterior() {
        // Given
        UUID id = UUID.randomUUID();
        EntregablesEntity entity = new EntregablesEntity();
        entity.setId(id);
        entity.setTipoEntregable(TipoEntregable.ARCHIVO);
        entity.setRuta("uploads/old_file.txt");

        MockMultipartFile newArchivo = new MockMultipartFile("archivo", "new.txt", "text/plain",
                "nuevo contenido".getBytes());
        EntregableDtoEntrada dto = EntregableDtoEntrada.builder()
                .tipoEntregable("ARCHIVO")
                .archivo(newArchivo)
                .build();

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));
        when(almacenamientoService.guardarArchivo(newArchivo)).thenReturn("uuid_new.txt");

        // When
        service.actualizarRecurso(id, dto);

        // Then
        verify(almacenamientoService).eliminarArchivo("uploads/old_file.txt");
        verify(almacenamientoService).guardarArchivo(newArchivo);
        assertEquals("new.txt", entity.getNombreArchivo());
        verify(repositorio).save(entity);
    }

    @Test
    void debeListarEntregablesPorTarea() {
        // Given
        UUID tareaId = UUID.randomUUID();
        List<EntregablesEntity> entities = List.of(new EntregablesEntity());
        List<EntregableDtoSalida> dtos = List.of(EntregableDtoSalida.builder().build());

        when(repositorio.findByTareaAsociadaId(tareaId)).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        // When
        List<EntregableDtoSalida> resultado = service.listarEntregables(tareaId.toString());

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repositorio).findByTareaAsociadaId(tareaId);
        verify(mapper).toDtoList(entities);
    }
}
