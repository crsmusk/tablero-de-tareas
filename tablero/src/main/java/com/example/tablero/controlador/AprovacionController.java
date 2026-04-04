package com.example.tablero.controlador;

import com.example.tablero.entidades.dtos.entrada.AprovacionDtoEntrada;
import com.example.tablero.entidades.dtos.salida.AprovacionesDtoSalida;
import com.example.tablero.servicio.interfaces.AprovacionI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/aprovaciones")
@Tag(name = "Aprobaciones", description = "Endpoints para la gestión de las aprobaciones de tareas")
public class AprovacionController {

    private AprovacionI aprovacionService;

    public AprovacionController(AprovacionI aprovacionService) {
        this.aprovacionService = aprovacionService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'CLIENT_VIEWER')")
    @Operation(summary = "Guardar un veredicto de aprobación", description = "Registra una nueva evaluación (aprobada/rechazada) para un entregable o tarea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veredicto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<Void> guardarVeredicto(
            @Parameter(description = "Objeto con los detalles de la aprobación a guardar") @Valid @RequestBody AprovacionDtoEntrada dto) {
        aprovacionService.guardarVeredicto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/tarea/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'CLIENT_VIEWER')")
    @Operation(summary = "Buscar aprobaciones por Tarea", description = "Devuelve el listado completo de aprobaciones asociadas a una tarea específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de aprobaciones recuperado correctamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<List<AprovacionesDtoSalida>> buscarPorTarea(
            @Parameter(description = "Identificador único (UUID) de la tarea") @PathVariable UUID id) {
        return ResponseEntity.ok(aprovacionService.buscarPorTarea(id));
    }
}
