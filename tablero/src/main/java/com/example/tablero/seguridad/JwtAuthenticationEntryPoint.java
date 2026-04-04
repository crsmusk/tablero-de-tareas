package com.example.tablero.seguridad;

import com.example.tablero.entidades.dtos.salida.ErrorDtoSalida;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

/**
 * Manejador que se activa cuando un usuario no autenticado intenta acceder
 * a un recurso protegido. Retorna un error 401 (Unauthorized).
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        ErrorDtoSalida error = ErrorDtoSalida.builder()
                .timestamp(LocalDateTime.now())
                .estado(HttpStatus.UNAUTHORIZED.value())
                .mensaje("Acceso denegado. Debe autenticarse para acceder a este recurso.")
                .detalles(request.getRequestURI())
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.writeValue(out, error);
        out.flush();
    }
}
