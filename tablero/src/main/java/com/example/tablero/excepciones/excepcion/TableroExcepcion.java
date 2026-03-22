package com.example.tablero.excepciones.excepcion;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TableroExcepcion extends RuntimeException {
    private final HttpStatus estadoHttp;

    public TableroExcepcion(String mensaje, HttpStatus estadoHttp) {
        super(mensaje);
        this.estadoHttp = estadoHttp;
    }
}
