package com.example.tablero.controlador;

import com.example.tablero.entidades.dtos.entrada.TareaDtoEntrada;
import com.example.tablero.entidades.dtos.salida.TareaDtoSalida;
import com.example.tablero.servicio.interfaces.TareaI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tarea")
// @CrossOrigin("*")
public class TareaControlador {

    @Autowired
    private TareaI tareaS;

    @PostMapping
    public ResponseEntity<Void> guardarTarea(@RequestBody @Valid TareaDtoEntrada tareaDto) {
        tareaS.guardarTarea(tareaDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaDtoSalida> buscarTareaPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(tareaS.buscarTareaPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<TareaDtoSalida>> listarTarea() {
        return ResponseEntity.ok(tareaS.listarTarea());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<TareaDtoSalida>> buscarTareaPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(tareaS.buscarTareaPorTitulo(titulo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarTarea(@PathVariable UUID id, @RequestBody @Valid TareaDtoEntrada tareaDto) {
        tareaS.actualizarTarea(id, tareaDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable UUID id) {
        tareaS.eliminarTarea(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/orden")
    public ResponseEntity<Void> cambiarOrden(@RequestParam UUID idAnterior, @RequestParam UUID idActual) {
        tareaS.cambiarOrden(idAnterior, idActual);
        return ResponseEntity.ok().build();
    }

}
