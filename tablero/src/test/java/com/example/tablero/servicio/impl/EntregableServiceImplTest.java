package com.example.tablero.servicio.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.tablero.entidades.dtos.entrada.EntregableDtoEntrada;
import com.example.tablero.entidades.dtos.salida.EntregableDtoSalida;
import com.example.tablero.entidades.entidades.EntregablesEntity;
import com.example.tablero.entidades.entidades.TareaEntity;
import com.example.tablero.entidades.entidades.enums.TipoEntregable;
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import com.example.tablero.mapper.EntregableMapper;
import com.example.tablero.repositorio.EntregablesRepositorio;
import com.example.tablero.repositorio.TareaRepositorio;

@ExtendWith(MockitoExtension.class)
class EntregableServiceImplTest {

    @Mock
    private EntregablesRepositorio repositorio;

    @Mock
    private EntregableMapper mapper;

    @Mock
    private TareaRepositorio tareasRepositorio;

    @InjectMocks
    private EntregableServiceImpl service;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        // Inyectamos el directorio temporal para evitar usar la carpeta real 'uploads'
        ReflectionTestUtils.setField(service, "rootLocation", tempDir);
    }

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
        when(repositorio.save(any(EntregablesEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        EntregablesEntity resultado = service.guardarEntregable(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(TipoEntregable.ENLACE, resultado.getTipoEntregable());
        assertEquals("http://google.com", resultado.getRecurso());
        assertEquals(tarea, resultado.getTareaAsociada());
        verify(repositorio).save(any(EntregablesEntity.class));
    }

    @Test
    void debeGuardarEntregableTipoArchivo() throws IOException {
        // Given
        MockMultipartFile archivo = new MockMultipartFile("archivo", "test.txt", "text/plain", "contenido".getBytes());
        EntregableDtoEntrada dto = EntregableDtoEntrada.builder()
                .tipoEntregable("ARCHIVO")
                .archivo(archivo)
                .build();

        when(repositorio.save(any(EntregablesEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        EntregablesEntity resultado = service.guardarEntregable(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(TipoEntregable.ARCHIVO, resultado.getTipoEntregable());
        assertEquals("test.txt", resultado.getNombreArchivo());
        assertTrue(resultado.getRuta().contains("test.txt"));
        assertTrue(Files.exists(tempDir.resolve(resultado.getRecurso())));
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
        // Simulamos 4 entregables existentes
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
    }

    @Test
    void debeEliminarEntregableTipoArchivoYBorrarFisico() throws IOException {
        // Given
        UUID id = UUID.randomUUID();
        Path dummyFile = tempDir.resolve("delete_me.txt");
        Files.createFile(dummyFile);

        EntregablesEntity entity = new EntregablesEntity();
        entity.setId(id);
        entity.setTipoEntregable(TipoEntregable.ARCHIVO);
        entity.setRuta(dummyFile.toString());

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));

        // When
        service.eliminarEntregable(id);

        // Then
        assertFalse(Files.exists(dummyFile));
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
    void debeActualizarDeArchivoAEnlaceYBorrarArchivoAnterior() throws IOException {
        // Given
        UUID id = UUID.randomUUID();
        Path oldFile = tempDir.resolve("old.txt");
        Files.createFile(oldFile);

        EntregablesEntity entity = new EntregablesEntity();
        entity.setId(id);
        entity.setTipoEntregable(TipoEntregable.ARCHIVO);
        entity.setRuta(oldFile.toString());

        EntregableDtoEntrada dto = EntregableDtoEntrada.builder()
                .tipoEntregable("ENLACE")
                .enlace("http://newlink.com")
                .build();

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));

        // When
        EntregablesEntity resultado = service.actualizarRecurso(id, dto);

        // Then
        assertEquals(TipoEntregable.ENLACE, resultado.getTipoEntregable());
        assertEquals("http://newlink.com", resultado.getRecurso());
        assertFalse(Files.exists(oldFile));
        verify(repositorio).save(entity);
    }

    @Test
    void debeActualizarArchivoYPisaroAnterior() throws IOException {
        // Given
        UUID id = UUID.randomUUID();
        Path oldFile = tempDir.resolve("old_file.txt");
        Files.createFile(oldFile);

        EntregablesEntity entity = new EntregablesEntity();
        entity.setId(id);
        entity.setTipoEntregable(TipoEntregable.ARCHIVO);
        entity.setRuta(oldFile.toString());

        MockMultipartFile newArchivo = new MockMultipartFile("archivo", "new.txt", "text/plain",
                "nuevo contenido".getBytes());
        EntregableDtoEntrada dto = EntregableDtoEntrada.builder()
                .tipoEntregable("ARCHIVO")
                .archivo(newArchivo)
                .build();

        when(repositorio.findById(id)).thenReturn(Optional.of(entity));

        // When
        EntregablesEntity resultado = service.actualizarRecurso(id, dto);

        // Then
        assertEquals(TipoEntregable.ARCHIVO, resultado.getTipoEntregable());
        assertEquals("new.txt", resultado.getNombreArchivo());
        assertFalse(Files.exists(oldFile));
        assertTrue(Files.exists(tempDir.resolve(resultado.getRecurso())));
        verify(repositorio).save(entity);
    }

    @Test
    void debeListarTodosLosEntregables() {
        // Given
        List<EntregablesEntity> entities = List.of(new EntregablesEntity());
        List<EntregableDtoSalida> dtos = List.of(EntregableDtoSalida.builder().build());

        when(repositorio.findAll()).thenReturn(entities);
        when(mapper.toDtoList(entities)).thenReturn(dtos);

        // When
        List<EntregableDtoSalida> resultado = service.listarEntregables();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repositorio).findAll();
        verify(mapper).toDtoList(entities);
    }
}
