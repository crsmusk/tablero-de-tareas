package com.example.tablero.controlador;

import com.example.tablero.entidades.dtos.entrada.EntregableDtoEntrada;
import com.example.tablero.entidades.dtos.salida.EntregableDtoSalida;
import com.example.tablero.servicio.interfaces.EntregableI;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/entregable")
public class EntregableControlador {

    private EntregableI entregableS;

    public EntregableControlador(EntregableI entregableS) {
        this.entregableS = entregableS;
    }

    @PostMapping
    public ResponseEntity<Void> guardarEntregable(
            @ModelAttribute @Valid EntregableDtoEntrada entregableDto) {
        entregableS.guardarEntregable(entregableDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<EntregableDtoSalida>> listarEntregables() {
        return ResponseEntity.ok(entregableS.listarEntregables());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarEntregable(@PathVariable UUID id,
            @ModelAttribute @Valid EntregableDtoEntrada entregableDto) {
        entregableS.actualizarRecurso(id, entregableDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEntregable(@PathVariable UUID id) {
        entregableS.eliminarEntregable(id);
        return ResponseEntity.noContent().build();
    }
}
