package com.example.tablero.servicio.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import com.example.tablero.excepciones.excepcion.TableroExcepcion;

class AlmacenamientoServiceImplTest {

    @TempDir
    Path tempDir;

    private AlmacenamientoServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AlmacenamientoServiceImpl(tempDir);
    }

    @Test
    void debeGuardarArchivoExitosamente() {
        // Given
        MockMultipartFile archivo = new MockMultipartFile("archivo", "documento.txt", "text/plain",
                "contenido de prueba".getBytes());

        // When
        String nombreUnico = service.guardarArchivo(archivo);

        // Then
        assertNotNull(nombreUnico);
        assertTrue(nombreUnico.contains("documento.txt"));
        assertTrue(Files.exists(tempDir.resolve(nombreUnico)));
    }

    @Test
    void debeLanzarExcepcionCuandoArchivoEsNulo() {
        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.guardarArchivo(null));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getEstadoHttp());
        assertEquals("Se requiere un archivo válido", ex.getMessage());
    }

    @Test
    void debeLanzarExcepcionCuandoArchivoEstaVacio() {
        // Given
        MockMultipartFile archivo = new MockMultipartFile("archivo", "vacio.txt", "text/plain", new byte[0]);

        // When & Then
        TableroExcepcion ex = assertThrows(TableroExcepcion.class, () -> service.guardarArchivo(archivo));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getEstadoHttp());
    }

    @Test
    void debeEliminarArchivoExitosamente() throws IOException {
        // Given
        Path archivo = tempDir.resolve("borrar_me.txt");
        Files.createFile(archivo);
        assertTrue(Files.exists(archivo));

        // When
        service.eliminarArchivo(archivo.toString());

        // Then
        assertFalse(Files.exists(archivo));
    }

    @Test
    void noDebeFallarCuandoRutaEsNula() {
        // When & Then — no debe lanzar excepción
        assertDoesNotThrow(() -> service.eliminarArchivo(null));
    }

    @Test
    void noDebeFallarCuandoArchivoNoExiste() {
        // When & Then — no debe lanzar excepción
        assertDoesNotThrow(() -> service.eliminarArchivo(tempDir.resolve("inexistente.txt").toString()));
    }
}
