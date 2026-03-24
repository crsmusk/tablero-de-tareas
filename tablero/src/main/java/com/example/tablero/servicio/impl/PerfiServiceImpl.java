package com.example.tablero.servicio.impl;

import com.example.tablero.entidades.dtos.entrada.PerfilDtoEntrada;
import com.example.tablero.entidades.dtos.salida.PerfilDtoSalida;
import com.example.tablero.entidades.entidades.PerfilEntity;
import com.example.tablero.mapper.PerfilMapper;
import com.example.tablero.repositorio.PerfilRepositorio;
import com.example.tablero.servicio.interfaces.PerfilI;
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PerfiServiceImpl implements PerfilI {

    private PerfilRepositorio repositorio;
    private PerfilMapper mapper;

    public PerfiServiceImpl(PerfilRepositorio repositorio, PerfilMapper mapper) {
        this.repositorio = repositorio;
        this.mapper = mapper;
    }

    @Override
    public void guardarPerfil(PerfilDtoEntrada perfilDto) {
        PerfilEntity usuario = new PerfilEntity();
        usuario.setContraseña(perfilDto.getContraseña());
        usuario.setNombre(perfilDto.getNombre());
        usuario.setNickName(perfilDto.getNickName());
        usuario.setCorreo(perfilDto.getCorreo());
        this.repositorio.save(usuario);
    }

    @Override
    public PerfilDtoSalida buscarPerfil(UUID id) {
        return mapper.PerfilM(this.repositorio.findById(id).get());
    }

    @Override
    public void eliminarPerfil(UUID id) {
        PerfilEntity perfil = this.repositorio.findById(id)
                .orElseThrow(() -> new TableroExcepcion("No se encontró el perfil con id " + id, HttpStatus.NOT_FOUND));
        this.repositorio.delete(perfil);
    }

    @Override
    public void actualizarPerfil(UUID id, PerfilDtoEntrada perfilDto) {
        PerfilEntity perfil = this.repositorio.findById(id)
                .orElseThrow(() -> new TableroExcepcion("No se encontró el perfil con id " + id, HttpStatus.NOT_FOUND));
        if (perfilDto.getCorreo() != null && !perfilDto.getCorreo().isEmpty()) {
            perfil.setCorreo(perfilDto.getCorreo());
        }
        if (perfilDto.getNombre() != null && !perfilDto.getNombre().isEmpty()) {
            perfil.setNombre(perfilDto.getNombre());
        }
        if (perfilDto.getContraseña() != null && !perfilDto.getContraseña().isEmpty()) {
            perfil.setContraseña(perfilDto.getContraseña());
        }
        if (perfilDto.getNickName() != null && !perfilDto.getNickName().isEmpty()) {
            perfil.setNickName(perfilDto.getNickName());
        }
        this.repositorio.save(perfil);
    }
}
