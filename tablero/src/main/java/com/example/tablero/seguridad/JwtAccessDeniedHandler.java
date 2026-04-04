package com.example.tablero.seguridad;

import com.example.tablero.entidades.dtos.salida.ErrorDtoSalida;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

/**
 * Manejador que se activa cuando un usuario autenticado no tiene los permisos
 * suficientes para acceder a un recurso. Retorna un error 403 (Forbidden).
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ErrorDtoSalida error = ErrorDtoSalida.builder()
                .timestamp(LocalDateTime.now())
                .estado(HttpStatus.FORBIDDEN.value())
                .mensaje("Acceso denegado. No tiene los permisos necesarios para realizar esta acción.")
                .detalles(request.getRequestURI())
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.writeValue(out, error);
        out.flush();
    }
}
