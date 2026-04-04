package com.example.tablero.entidades.dtos.salida;

public record JwtResponse(
        String token,
        String tipoToken) {
    public JwtResponse(String token) {
        this(token, "Bearer");
    }
}
