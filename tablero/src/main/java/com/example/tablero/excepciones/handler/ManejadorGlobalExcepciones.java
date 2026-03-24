package com.example.tablero.excepciones.handler;

import com.example.tablero.entidades.dtos.salida.ErrorDtoSalida;
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(TableroExcepcion.class)
    public ResponseEntity<ErrorDtoSalida> manejarTableroExcepcion(TableroExcepcion ex, WebRequest request) {
        ErrorDtoSalida errorDefecto = ErrorDtoSalida.builder()
                .timestamp(LocalDateTime.now())
                .estado(ex.getEstadoHttp().value())
                .mensaje(ex.getMessage())
                .detalles(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorDefecto, ex.getEstadoHttp());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDtoSalida> manejarValidaciones(MethodArgumentNotValidException ex, WebRequest request) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorDtoSalida errorDefecto = ErrorDtoSalida.builder()
                .timestamp(LocalDateTime.now())
                .estado(HttpStatus.BAD_REQUEST.value())
                .mensaje("Error de validación: " + mensaje)
                .detalles(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorDefecto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class })
    public ResponseEntity<ErrorDtoSalida> manejarTiposInvalidos(Exception ex, WebRequest request) {
        ErrorDtoSalida errorDefecto = ErrorDtoSalida.builder()
                .timestamp(LocalDateTime.now())
                .estado(HttpStatus.BAD_REQUEST.value())
                .mensaje("El formato o tipo de dato proporcionado en la petición es inválido.")
                .detalles(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorDefecto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDtoSalida> manejarExcepcionesGlobales(Exception ex, WebRequest request) {
        log.error("Excepción interna no controlada capturada por el manejador global: ", ex);

        ErrorDtoSalida errorDefecto = ErrorDtoSalida.builder()
                .timestamp(LocalDateTime.now())
                .estado(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .mensaje("Se ha producido un error interno en el servidor. Contacte a soporte técnico.")
                .detalles(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorDefecto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
