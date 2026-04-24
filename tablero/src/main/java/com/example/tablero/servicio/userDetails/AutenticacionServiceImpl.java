package com.example.tablero.servicio.userDetails;

import com.example.tablero.entidades.dtos.entrada.LoginRequest;
import com.example.tablero.entidades.dtos.salida.JwtResponse;
import com.example.tablero.entidades.entidades.PerfilEntity;
import com.example.tablero.repositorio.PerfilRepositorio;
import com.example.tablero.seguridad.JwtUtiles;
import com.example.tablero.servicio.interfaces.AutenticacionI;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service("autenticacionServiceSecurity")
public class AutenticacionServiceImpl implements AutenticacionI {

    private final AuthenticationManager authenticationManager;
    private final JwtUtiles jwtUtiles;
    private final PerfilRepositorio perfilRepositorio;

    public AutenticacionServiceImpl(AuthenticationManager authenticationManager, JwtUtiles jwtUtiles,
            PerfilRepositorio perfilRepositorio) {
        this.authenticationManager = authenticationManager;
        this.jwtUtiles = jwtUtiles;
        this.perfilRepositorio = perfilRepositorio;
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.correo(),
                        request.contraseña()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        PerfilEntity perfil = perfilRepositorio.findByCorreo(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado tras autenticación"));

        String token = jwtUtiles.generarToken(userDetails, perfil.getId());

        return new JwtResponse(token);
    }
}
