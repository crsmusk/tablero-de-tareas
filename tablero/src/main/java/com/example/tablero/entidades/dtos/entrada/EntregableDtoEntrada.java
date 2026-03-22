package com.example.tablero.entidades.dtos.entrada;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EntregableDtoEntrada {
    private String idTarea;
    private String enlace;
    private MultipartFile archivo;
    private String tipoEntregable;
    private String nombreArchivo;
}
