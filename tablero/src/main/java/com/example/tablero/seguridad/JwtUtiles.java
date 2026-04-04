package com.example.tablero.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtiles {

    @Value("${jwt.user.secret}")
    private String userSecretKey;

    @Value("${jwt.user.expiration}")
    private long userJwtExpiration;

    @Value("${jwt.client.secret}")
    private String clientSecretKey;

    @Value("${jwt.client.expiration}")
    private long clientJwtExpiration;

    // --- JWT PARA USUARIOS NORMALES ---

    public String generarToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        extraClaims.put("roles", roles);

        return construirToken(extraClaims, userDetails.getUsername(), userJwtExpiration, obtenerLlave(userSecretKey));
    }

    public boolean esTokenValido(String token, UserDetails userDetails) {
        final String correo = extraerCorreoUsuario(token);
        return (correo.equals(userDetails.getUsername())) && !esTokenExpirado(token, userSecretKey);
    }

    public String extraerCorreoUsuario(String token) {
        return extraerCorreo(token, userSecretKey);
    }

    // --- JWT PARA CLIENTES (ENLACES TEMPORALES RESTRINGIDOS) ---

    public String generarTokenCliente(String identificadorPeticion) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", "ROLE_CLIENT_VIEWER");

        return construirToken(extraClaims, identificadorPeticion, clientJwtExpiration, obtenerLlave(clientSecretKey));
    }

    public boolean esTokenClienteValido(String token, String identificadorEsperado) {
        final String identificador = extraerIdentificadorCliente(token);
        return (identificador.equals(identificadorEsperado)) && !esTokenExpirado(token, clientSecretKey);
    }

    public String extraerIdentificadorCliente(String token) {
        return extraerCorreo(token, clientSecretKey);
    }

    private String construirToken(Map<String, Object> extraClaims, String subject, long expiration,
            SecretKey llaveFirma) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(llaveFirma, Jwts.SIG.HS256)
                .compact();
    }

    private String extraerCorreo(String token, String secretStr) {
        return extraerClaim(token, Claims::getSubject, secretStr);
    }

    private <T> T extraerClaim(String token, Function<Claims, T> claimsResolver, String secretStr) {
        final Claims claims = extraerTodosLosClaims(token, secretStr);
        return claimsResolver.apply(claims);
    }

    private boolean esTokenExpirado(String token, String secretStr) {
        return extraerExpiracion(token, secretStr).before(new Date());
    }

    private Date extraerExpiracion(String token, String secretStr) {
        return extraerClaim(token, Claims::getExpiration, secretStr);
    }

    private Claims extraerTodosLosClaims(String token, String secretStr) {
        return Jwts.parser()
                .verifyWith(obtenerLlave(secretStr))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey obtenerLlave(String secretStr) {
        byte[] keyBytes = Decoders.BASE64.decode(secretStr);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
