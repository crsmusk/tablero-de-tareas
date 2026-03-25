package com.example.tablero.controlador;

import com.example.tablero.entidades.dtos.entrada.PerfilDtoEntrada;
import com.example.tablero.entidades.dtos.salida.PerfilDtoSalida;
import com.example.tablero.servicio.interfaces.PerfilI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/perfil")
public class PerfilControlador {

    @Autowired
    private PerfilI perfilS;

    @PostMapping
    public ResponseEntity<Void> guardarPerfil(@RequestBody @Valid PerfilDtoEntrada perfilDto) {
        perfilS.guardarPerfil(perfilDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilDtoSalida> buscarPerfil(@PathVariable UUID id) {
        return ResponseEntity.ok(perfilS.buscarPerfil(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarPerfil(@PathVariable UUID id,
            @RequestBody @Valid PerfilDtoEntrada perfilDto) {
        perfilS.actualizarPerfil(id, perfilDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPerfil(@PathVariable UUID id) {
        perfilS.eliminarPerfil(id);
        return ResponseEntity.noContent().build();
    }
}
