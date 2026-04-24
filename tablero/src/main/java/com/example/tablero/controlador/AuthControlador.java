package com.example.tablero.controlador;

import com.example.tablero.entidades.dtos.entrada.LoginRequest;
import com.example.tablero.entidades.dtos.salida.JwtResponse;
import com.example.tablero.servicio.interfaces.AutenticacionI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para inicio de sesión y gestión de tokens")
public class AuthControlador {

    private final AutenticacionI autenticacionService;

    public AuthControlador(@Qualifier("autenticacionServiceSecurity") AutenticacionI autenticacionService) {
        this.autenticacionService = autenticacionService;
    }

    @PostMapping("/login")
    @Operation(summary = "Inicio de sesión", description = "Permite a un usuario autenticarse y recibir un token JWT")
    @ApiResponse(responseCode = "200", description = "Autenticación exitosa")
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(autenticacionService.login(request));
    }
}
