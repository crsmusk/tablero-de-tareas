package com.example.tablero.servicio.interfaces;

import com.example.tablero.entidades.entidades.RolEntity;
import com.example.tablero.entidades.entidades.enums.RolNombre;

public interface RolI {

    RolEntity buscarPorNombre(RolNombre rolNombre);
}
