package com.example.tablero.controlador;

import com.example.tablero.entidades.dtos.entrada.TareaDtoEntrada;
import com.example.tablero.entidades.dtos.salida.TareaDtoSalida;
import com.example.tablero.servicio.interfaces.TareaI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tarea")
// @CrossOrigin("*")
@Tag(name = "Tareas", description = "Endpoints para la gestión de las tareas correspondientes a los proyectos")
public class TareaControlador {

    private TareaI tareaS;

    public TareaControlador(TareaI tareaS) {
        this.tareaS = tareaS;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Guardar una nueva tarea", description = "Registra una nueva tarea dentro de un proyecto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarea registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos provistos inválidos")
    })
    public ResponseEntity<Void> guardarTarea(
            @Parameter(description = "Información de la tarea") @RequestBody @Valid TareaDtoEntrada tareaDto) {
        tareaS.guardarTarea(tareaDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'CLIENT_VIEWER')")
    @Operation(summary = "Consultar tarea por ID", description = "Devuelve la información de una tarea específica con sus dependencias correspondientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<TareaDtoSalida> buscarTareaPorId(
            @Parameter(description = "UUID de la tarea") @PathVariable UUID id) {
        return ResponseEntity.ok(tareaS.buscarTareaPorId(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Listar todas las tareas", description = "Devuelve una lista con todas las tareas registradas")
    @ApiResponse(responseCode = "200", description = "Listado de tareas recuperado exitosamente")
    public ResponseEntity<List<TareaDtoSalida>> listarTarea() {
        return ResponseEntity.ok(tareaS.listarTarea());
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Buscar tarea por título", description = "Permite la búsqueda de tareas que coincidan con un título específico")
    @ApiResponse(responseCode = "200", description = "Listado de tareas encontradas")
    public ResponseEntity<List<TareaDtoSalida>> buscarTareaPorTitulo(
            @Parameter(description = "Título o aproximación del título a buscar") @RequestParam String titulo) {
        return ResponseEntity.ok(tareaS.buscarTareaPorTitulo(titulo));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Actualizar tarea", description = "Actualiza toda la información de una tarea existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<Void> actualizarTarea(
            @Parameter(description = "UUID de la tarea a actualizar") @PathVariable UUID id,
            @Parameter(description = "Datos actualizados de la tarea") @RequestBody @Valid TareaDtoEntrada tareaDto) {
        tareaS.actualizarTarea(id, tareaDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Eliminar tarea", description = "Elimina de forma irreversible una tarea del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarea eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<Void> eliminarTarea(@Parameter(description = "UUID de la tarea") @PathVariable UUID id) {
        tareaS.eliminarTarea(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/orden")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Cambiar orden de tareas", description = "Reorganiza el orden secuencial de una tarea frente a otra")
    @ApiResponse(responseCode = "200", description = "Orden modificado correctamente")
    public ResponseEntity<Void> cambiarOrden(
            @Parameter(description = "UUID de la tarea en la posición anterior") @RequestParam UUID idAnterior,
            @Parameter(description = "UUID de la tarea en la posición actual a mover") @RequestParam UUID idActual) {
        tareaS.cambiarOrden(idAnterior, idActual);
        return ResponseEntity.ok().build();
    }

}
