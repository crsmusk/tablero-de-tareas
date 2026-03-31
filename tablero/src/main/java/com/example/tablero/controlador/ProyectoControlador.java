package com.example.tablero.controlador;

import com.example.tablero.entidades.dtos.salida.ProyectoResumidoDtoSalida;
import com.example.tablero.servicio.interfaces.ProyectoI;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import com.example.tablero.entidades.dtos.entrada.ProyectoDtoEntrada;
import com.example.tablero.entidades.dtos.salida.ProyectoDtoSalida;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/proyectos")
public class ProyectoControlador {

    private ProyectoI proyectoS;

    public ProyectoControlador(ProyectoI proyectoS) {
        this.proyectoS = proyectoS;
    }

    @PostMapping
    public ResponseEntity<String> guardarProyecto(@RequestBody @Valid ProyectoDtoEntrada proyectoDto) {
        return ResponseEntity.ok(proyectoS.guardarProyecto(proyectoDto));
    }

    @GetMapping
    public ResponseEntity<List<ProyectoResumidoDtoSalida>> listarProyectos() {
        return ResponseEntity.ok(proyectoS.listaProyectos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProyectoDtoSalida> buscarProyecto(@PathVariable UUID id) {
        return ResponseEntity.ok(proyectoS.buscarProyecto(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarProyecto(@PathVariable UUID id,
            @RequestBody @Valid ProyectoDtoEntrada proyectoDto) {
        proyectoS.actualizarProyecto(id, proyectoDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProyecto(@PathVariable UUID id) {
        proyectoS.eliminarProyecto(id);
        return ResponseEntity.noContent().build();
    }
}
