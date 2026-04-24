package com.example.tablero.servicio.userDetails;

import com.example.tablero.entidades.entidades.PerfilEntity;
import com.example.tablero.repositorio.PerfilRepositorio;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class PersonaDetailServiceImpl implements UserDetailsService {

    private final PerfilRepositorio perfilRepositorio;

    public PersonaDetailServiceImpl(PerfilRepositorio perfilRepositorio) {
        this.perfilRepositorio = perfilRepositorio;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        PerfilEntity perfil = perfilRepositorio.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Acceso denegado. No se encontró cuenta con el correo: " + correo));

        return User.builder()
                .username(perfil.getCorreo())
                .password(perfil.getContraseña())
                .authorities(perfil.getRoles().stream()
                        .map(rol -> new SimpleGrantedAuthority(rol.getRolNombre().name()))
                        .collect(Collectors.toList()))
                .build();
    }
}
