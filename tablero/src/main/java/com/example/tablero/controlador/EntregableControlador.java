package com.example.tablero.controlador;

import com.example.tablero.entidades.dtos.entrada.EntregableDtoEntrada;
import com.example.tablero.entidades.dtos.salida.EntregableDtoSalida;
import com.example.tablero.servicio.interfaces.EntregableI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/entregable")
@Tag(name = "Entregables", description = "Endpoints para la gestión de entregables de los proyectos o tareas")
public class EntregableControlador {

    private EntregableI entregableS;

    public EntregableControlador(EntregableI entregableS) {
        this.entregableS = entregableS;
    }

    @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Guardar un entregable", description = "Registra un nuevo entregable asociado a una tarea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entregable guardado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<Void> guardarEntregable(
            @Parameter(description = "Datos del entregable a guardar") @ModelAttribute @Valid EntregableDtoEntrada entregableDto) {
        entregableS.guardarEntregable(entregableDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @Operation(summary = "Listar todos los entregables", description = "Devuelve una lista con todos los entregables registrados")
    @ApiResponse(responseCode = "200", description = "Listado recuperado correctamente")
    public ResponseEntity<List<EntregableDtoSalida>> listarEntregables() {
        return ResponseEntity.ok(entregableS.listarEntregables());
    }

    @PutMapping(value = "/{id}", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Actualizar entregable", description = "Modifica los datos de un entregable existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entregable actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entregable no encontrado")
    })
    public ResponseEntity<Void> actualizarEntregable(
            @Parameter(description = "UUID del entregable a actualizar") @PathVariable UUID id,
            @Parameter(description = "Datos actualizados del entregable") @ModelAttribute @Valid EntregableDtoEntrada entregableDto) {
        entregableS.actualizarRecurso(id, entregableDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar entregable", description = "Elimina un entregable específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entregable eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Entregable no encontrado")
    })
    public ResponseEntity<Void> eliminarEntregable(
            @Parameter(description = "UUID del entregable a eliminar") @PathVariable UUID id) {
        entregableS.eliminarEntregable(id);
        return ResponseEntity.noContent().build();
    }
}
