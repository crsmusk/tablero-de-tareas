package com.example.tablero.entidades.dtos.salida;

public record AuthResponse(
        String token,
        String tipoToken) {
    public AuthResponse(String token) {
        this(token, "Bearer");
    }
}
