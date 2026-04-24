package com.example.tablero.controlador;

import com.example.tablero.entidades.dtos.entrada.PerfilDtoEntrada;
import com.example.tablero.entidades.dtos.salida.PerfilDtoSalida;
import com.example.tablero.servicio.interfaces.PerfilI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/perfil")
@Tag(name = "Perfiles", description = "Endpoints para la gestión de los perfiles de usuario")
public class PerfilControlador {

    private PerfilI perfilS;

    public PerfilControlador(PerfilI perfilS) {
        this.perfilS = perfilS;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo perfil", description = "Registra un perfil de usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Perfil creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<Void> guardarPerfil(
            @Parameter(description = "Datos del perfil a crear") @RequestBody @Valid PerfilDtoEntrada perfilDto) {
        perfilS.guardarPerfil(perfilDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/buscarPerfil")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Buscar perfil por ID", description = "Obtiene los detalles de un perfil por su UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil recuperado correctamente"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    public ResponseEntity<PerfilDtoSalida> buscarPerfil() {
        return ResponseEntity.ok(perfilS.buscarPerfil());
    }

    @PutMapping("/actualizarPerfil")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Actualizar perfil", description = "Modifica los datos de un perfil existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    public ResponseEntity<Void> actualizarPerfil(
            @Parameter(description = "Nuevos datos del perfil") @RequestBody @Valid PerfilDtoEntrada perfilDto) {
        perfilS.actualizarPerfil(perfilDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar perfil", description = "Elimina un perfil del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Perfil eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    public ResponseEntity<Void> eliminarPerfil(
            @Parameter(description = "UUID del perfil a eliminar") @PathVariable UUID id) {
        perfilS.eliminarPerfil(id);
        return ResponseEntity.noContent().build();
    }
}
