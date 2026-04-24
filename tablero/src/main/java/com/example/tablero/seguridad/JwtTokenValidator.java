package com.example.tablero.seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
public class JwtTokenValidator extends OncePerRequestFilter {

    private final JwtUtiles jwtUtiles;
    private final UserDetailsService userDetailsService;

    public JwtTokenValidator(JwtUtiles jwtUtiles, UserDetailsService userDetailsService) {
        this.jwtUtiles = jwtUtiles;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {
            final String correoUsuario = jwtUtiles.extraerCorreoUsuario(jwt);

            if (correoUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(correoUsuario);

                if (jwtUtiles.esTokenValido(jwt, userDetails)) {
                    UUID userId = jwtUtiles.extraerUserId(jwt);
                    iniciarSesionEnContexto(request, userDetails, userDetails.getAuthorities(), userId);
                }
            }
        } catch (Exception e) {
            try {
                final String recursoSujetoId = jwtUtiles.extraerIdentificadorCliente(jwt);

                if (recursoSujetoId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtiles.esTokenClienteValido(jwt, recursoSujetoId)) {

                        List<GrantedAuthority> restriccionCliente = List
                                .of(new SimpleGrantedAuthority("ROLE_CLIENT_VIEWER"));

                        UserDetails clienteDetails = org.springframework.security.core.userdetails.User.builder()
                                .username(recursoSujetoId)
                                .password("")
                                .authorities(restriccionCliente)
                                .build();

                        iniciarSesionEnContexto(request, clienteDetails, restriccionCliente, null);
                    }
                }
            } catch (Exception ex) {
            }
        }

        filterChain.doFilter(request, response);
    }

    private void iniciarSesionEnContexto(HttpServletRequest request, UserDetails userDetails,
            Collection<? extends GrantedAuthority> authorities, UUID userId) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                userId,
                authorities);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
