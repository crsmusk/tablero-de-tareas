package com.example.tablero.servicio.impl;

import com.example.tablero.entidades.entidades.RolEntity;
import com.example.tablero.entidades.entidades.enums.RolNombre;
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import com.example.tablero.repositorio.RolRepositorio;
import com.example.tablero.servicio.interfaces.RolI;

import java.util.Optional;

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
        Optional<RolEntity> rol = rolRepositorio.findByRolNombre(rolNombre);
        if (rol.isEmpty()) {
            RolEntity rolEntity = new RolEntity();
            rolEntity.setRolNombre(rolNombre);
            return rolEntity;
        }
        return rol.get();
    }
}
