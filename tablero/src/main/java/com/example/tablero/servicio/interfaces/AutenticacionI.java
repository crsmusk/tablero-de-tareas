package com.example.tablero.servicio.interfaces;

import com.example.tablero.entidades.dtos.entrada.LoginRequest;
import com.example.tablero.entidades.dtos.salida.JwtResponse;

/**
 * Interfaz que define los métodos para la autenticación de usuarios.
 */
public interface AutenticacionI {

    /**
     * Realiza el proceso de login y genera un token JWT.
     * 
     * @param request Datos de acceso (correo y contraseña).
     * @return Respuesta con el token generado.
     */
    JwtResponse login(LoginRequest request);
}
