package com.example.tablero.servicio.impl;

import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import com.example.tablero.servicio.interfaces.AlmacenamientoI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class AlmacenamientoServiceImpl implements AlmacenamientoI {

    private final Path rootLocation;

    public AlmacenamientoServiceImpl() {
        this.rootLocation = Paths.get("uploads");
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new TableroExcepcion("No se pudo inicializar la carpeta de almacenamiento",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Constructor para testing — permite inyectar un directorio temporal
    public AlmacenamientoServiceImpl(Path rootLocation) {
        this.rootLocation = rootLocation;
    }

    @Override
    public String guardarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new TableroExcepcion("Se requiere un archivo válido", HttpStatus.BAD_REQUEST);
        }
        try {
            String nombreUnico = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
            Files.copy(archivo.getInputStream(), this.rootLocation.resolve(nombreUnico));
            return nombreUnico;
        } catch (IOException e) {
            throw new TableroExcepcion("Error al guardar el archivo en disco", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void eliminarArchivo(String ruta) {
        if (ruta == null) {
            return;
        }
        try {
            Path archivo = Paths.get(ruta);
            Files.deleteIfExists(archivo);
        } catch (IOException e) {
            log.error("No se pudo eliminar el archivo físico: {}", e.getMessage());
        }
    }

    public Path getRootLocation() {
        return rootLocation;
    }
}
