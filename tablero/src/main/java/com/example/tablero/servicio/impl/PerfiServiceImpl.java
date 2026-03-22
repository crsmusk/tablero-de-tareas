package com.example.tablero.servicio.impl;

import com.example.tablero.entidades.dtos.entrada.PerfilDtoEntrada;
import com.example.tablero.entidades.dtos.salida.PerfilDtoSalida;
import com.example.tablero.entidades.entidades.PerfilEntity;
import com.example.tablero.mapper.PerfilMapper;
import com.example.tablero.repositorio.PerfilRepositorio;
import com.example.tablero.servicio.interfaces.PerfilI;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PerfiServiceImpl implements PerfilI {

    private PerfilRepositorio repositorio;
    private PerfilMapper mapper;
    public PerfiServiceImpl(PerfilRepositorio repositorio,PerfilMapper mapper) {
        this.repositorio = repositorio;
        this.mapper=mapper;
    }

    @Override
    public void guardarPerfil(PerfilDtoEntrada perfilDto) {
        PerfilEntity usuario=new PerfilEntity();
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
       this.repositorio.deleteById(id);
    }

    @Override
    public void actualizarPerfil(UUID id, PerfilDtoEntrada perfilDto) {
       PerfilEntity perfil=this.repositorio.findById(id)
               .orElseThrow(()->new RuntimeException("no se encontro el usuario con el id"+id));
       perfil.setCorreo(perfilDto.getCorreo());
       perfil.setNombre(perfilDto.getNombre());
       perfil.setContraseña(perfilDto.getContraseña());
       perfil.setNickName(perfilDto.getNickName());
       this.repositorio.save(perfil);
    }
}
