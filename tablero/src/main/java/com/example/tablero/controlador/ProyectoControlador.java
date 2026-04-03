package com.example.tablero.controlador;

import com.example.tablero.entidades.dtos.salida.ProyectoResumidoDtoSalida;
import com.example.tablero.servicio.interfaces.ProyectoI;
import com.example.tablero.entidades.dtos.entrada.ProyectoDtoEntrada;
import com.example.tablero.entidades.dtos.salida.ProyectoDtoSalida;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/proyectos")
@Tag(name = "Proyectos", description = "Endpoints para la gestión de proyectos")
public class ProyectoControlador {

    private ProyectoI proyectoS;

    public ProyectoControlador(ProyectoI proyectoS) {
        this.proyectoS = proyectoS;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo proyecto", description = "Registra un nuevo proyecto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto guardado exitosamente (retorna un mensaje confirmación)"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<String> guardarProyecto(
            @Parameter(description = "Datos del proyecto a registrar") @RequestBody @Valid ProyectoDtoEntrada proyectoDto) {
        return ResponseEntity.ok(proyectoS.guardarProyecto(proyectoDto));
    }

    @GetMapping
    @Operation(summary = "Listar proyectos resumidos", description = "Devuelve el listado de todos los proyectos con información resumida")
    @ApiResponse(responseCode = "200", description = "Listado recuperado correctamente")
    public ResponseEntity<List<ProyectoResumidoDtoSalida>> listarProyectos() {
        return ResponseEntity.ok(proyectoS.listaProyectos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un proyecto", description = "Devuelve todos los detalles de un proyecto en específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto recuperado correctamente"),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    public ResponseEntity<ProyectoDtoSalida> buscarProyecto(
            @Parameter(description = "UUID del proyecto") @PathVariable UUID id) {
        return ResponseEntity.ok(proyectoS.buscarProyecto(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar proyecto", description = "Actualiza los datos de un proyecto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proyecto actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    public ResponseEntity<Void> actualizarProyecto(
            @Parameter(description = "UUID del proyecto a actualizar") @PathVariable UUID id,
            @Parameter(description = "Datos a actualizar del proyecto") @RequestBody @Valid ProyectoDtoEntrada proyectoDto) {
        proyectoS.actualizarProyecto(id, proyectoDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar proyecto", description = "Elimina un proyecto del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proyecto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    public ResponseEntity<Void> eliminarProyecto(
            @Parameter(description = "UUID del proyecto a eliminar") @PathVariable UUID id) {
        proyectoS.eliminarProyecto(id);
        return ResponseEntity.noContent().build();
    }
}
