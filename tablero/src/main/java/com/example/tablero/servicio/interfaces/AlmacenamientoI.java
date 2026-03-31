package com.example.tablero.servicio.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface AlmacenamientoI {

    /**
     * Guarda un archivo en el directorio de uploads.
     *
     * @param archivo el MultipartFile recibido desde el controlador.
     * @return el nombre único generado para el archivo almacenado.
     */
    String guardarArchivo(MultipartFile archivo);

    /**
     * Elimina un archivo del directorio de uploads.
     *
     * @param ruta la ruta física del archivo a eliminar.
     */
    void eliminarArchivo(String ruta);
}
