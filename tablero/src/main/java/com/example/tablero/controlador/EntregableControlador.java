package com.example.tablero.controlador;

import com.example.tablero.entidades.dtos.entrada.EntregableDtoEntrada;
import com.example.tablero.entidades.dtos.salida.EntregableDtoSalida;
import com.example.tablero.entidades.entidades.EntregablesEntity;
import com.example.tablero.servicio.interfaces.EntregableI;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/entregable")
// @CrossOrigin("*")
public class EntregableControlador {

    @Autowired
    private EntregableI entregableS;

    @PostMapping
    public ResponseEntity<EntregablesEntity> guardarEntregable(
            @ModelAttribute @Valid EntregableDtoEntrada entregableDto) {
        EntregablesEntity nuevoEntregable = entregableS.guardarEntregable(entregableDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEntregable);
    }

    @GetMapping
    public ResponseEntity<List<EntregableDtoSalida>> listarEntregables() {
        return ResponseEntity.ok(entregableS.listarEntregables());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntregablesEntity> actualizarEntregable(@PathVariable UUID id,
            @ModelAttribute @Valid EntregableDtoEntrada entregableDto) {
        EntregablesEntity entregableActualizado = entregableS.actualizarRecurso(id, entregableDto);
        return ResponseEntity.ok(entregableActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEntregable(@PathVariable UUID id) {
        entregableS.eliminarEntregable(id);
        return ResponseEntity.noContent().build();
    }
}
