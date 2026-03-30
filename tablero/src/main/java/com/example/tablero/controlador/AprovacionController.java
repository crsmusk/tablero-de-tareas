package com.example.tablero.controlador;

import com.example.tablero.entidades.dtos.entrada.AprovacionDtoEntrada;
import com.example.tablero.entidades.dtos.salida.AprovacionesDtoSalida;
import com.example.tablero.servicio.interfaces.AprovacionI;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/aprovaciones")
@RequiredArgsConstructor
public class AprovacionController {

    private final AprovacionI aprovacionService;

    @PostMapping
    public ResponseEntity<Void> guardarVeredicto(@Valid @RequestBody AprovacionDtoEntrada dto) {
        aprovacionService.guardarVeredicto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/tarea/{id}")
    public ResponseEntity<List<AprovacionesDtoSalida>> buscarPorTarea(@PathVariable UUID id) {
        return ResponseEntity.ok(aprovacionService.buscarPorTarea(id));
    }
}
