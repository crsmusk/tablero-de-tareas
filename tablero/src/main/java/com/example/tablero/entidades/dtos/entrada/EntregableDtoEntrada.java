package com.example.tablero.entidades.dtos.entrada;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EntregableDtoEntrada {
    private String idTarea;
    private String enlace;
    private MultipartFile archivo;
    @NotBlank(message = "El tipo de entregable es obligatorio")
    @Pattern(regexp = "^(ARCHIVO|ENLACE)$", message = "El tipo debe ser ARCHIVO o ENLACE")
    private String tipoEntregable;
    private String nombreArchivo;
}
