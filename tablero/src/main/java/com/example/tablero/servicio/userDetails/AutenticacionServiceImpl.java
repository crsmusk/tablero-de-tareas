package com.example.tablero.servicio.userDetails;

import com.example.tablero.entidades.dtos.entrada.LoginRequest;
import com.example.tablero.entidades.dtos.salida.JwtResponse;
import com.example.tablero.seguridad.JwtUtiles;
import com.example.tablero.servicio.interfaces.AutenticacionI;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionServiceImpl implements AutenticacionI {

    private final AuthenticationManager authenticationManager;
    private final JwtUtiles jwtUtiles;

    public AutenticacionServiceImpl(AuthenticationManager authenticationManager, JwtUtiles jwtUtiles) {
        this.authenticationManager = authenticationManager;
        this.jwtUtiles = jwtUtiles;
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.correo(), request.contraseña()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtUtiles.generarToken(userDetails);

        return new JwtResponse(token);
    }
}
