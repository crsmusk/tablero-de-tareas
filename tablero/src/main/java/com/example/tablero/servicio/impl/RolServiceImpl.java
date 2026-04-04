package com.example.tablero.servicio.impl;

import com.example.tablero.entidades.entidades.RolEntity;
import com.example.tablero.entidades.entidades.enums.RolNombre;
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import com.example.tablero.repositorio.RolRepositorio;
import com.example.tablero.servicio.interfaces.RolI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RolServiceImpl implements RolI {

    private RolRepositorio rolRepositorio;

    public RolServiceImpl(RolRepositorio rolRepositorio) {
        this.rolRepositorio = rolRepositorio;
    }

    @Override
    public RolEntity buscarPorNombre(RolNombre rolNombre) {
        return rolRepositorio.findByRolNombre(rolNombre)
                .orElseThrow(() -> new TableroExcepcion("El rol especificado no existe en el sistema: " + rolNombre,
                        HttpStatus.NOT_FOUND));
    }
}
