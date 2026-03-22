package com.example.tablero.servicio.impl;

import com.example.tablero.entidades.dtos.entrada.ProyectoDtoEntrada;
import com.example.tablero.entidades.dtos.salida.ProyectoDtoSalida;
import com.example.tablero.entidades.dtos.salida.ProyectoResumidoDtoSalida;
import com.example.tablero.entidades.entidades.PerfilEntity;
import com.example.tablero.entidades.entidades.ProyectoEntity;
import com.example.tablero.mapper.ProyectoMapper;
import com.example.tablero.repositorio.PerfilRepositorio;
import com.example.tablero.repositorio.ProyectoRepositorio;
import com.example.tablero.servicio.interfaces.ProyectoI;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProyectoServiceImpl implements ProyectoI {

    private ProyectoRepositorio repositorio;
    private PerfilRepositorio perfilRepositorio;
    private ProyectoMapper mapper;

    public ProyectoServiceImpl(ProyectoRepositorio repositorio, PerfilRepositorio perfilRepositorio,
            ProyectoMapper mapper) {
        this.repositorio = repositorio;
        this.perfilRepositorio = perfilRepositorio;
        this.mapper = mapper;
    }

    @Override
    public String guardarTarea(ProyectoDtoEntrada proyectoDto) {
        if (proyectoDto.getIdPerfil() == null
                || !perfilRepositorio.existsById(UUID.fromString(proyectoDto.getIdPerfil()))) {
            throw new RuntimeException("no se encontro al usuario con el id " + proyectoDto.getIdPerfil());
        }
        PerfilEntity usuario = new PerfilEntity();
        usuario.setId(UUID.fromString(proyectoDto.getIdPerfil()));
        ProyectoEntity proyecto = new ProyectoEntity();
        proyecto.setNombreProyecto(proyectoDto.getNombreProyecto());
        proyecto.setCorreoCliente(proyectoDto.getCorreoCliente());
        proyecto.setNombreCliente(proyectoDto.getNombreCliente());
        proyecto.setPerfilAsociado(usuario);
        this.repositorio.save(proyecto);
        return "proximamente se agregara esto";
    }

    @Override
    public List<ProyectoResumidoDtoSalida> listaProyectos() {
        return mapper.ProyectosResumidoM(repositorio.findAll());
    }

    @Override
    public ProyectoDtoSalida buscarProyecto(UUID id) {
        return mapper.proyectoM(
                repositorio.findById(id).orElseThrow(() -> new RuntimeException("no se encontro el usuario")),
                repositorio.contarTareasCompletadas(id),
                repositorio.contarTareasPendientes(id),
                repositorio.contarTareasEnProgreso(id),
                repositorio.contarTareasEnRevision(id));
    }

    @Override
    public void actualizarProyecto(UUID id, ProyectoDtoEntrada proyectoDto) {
        ProyectoEntity proyecto = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("no se encontro el proyecto"));
        proyecto.setNombreProyecto(proyectoDto.getNombreProyecto());
        proyecto.setCorreoCliente(proyectoDto.getCorreoCliente());
        proyecto.setNombreCliente(proyectoDto.getNombreCliente());
        repositorio.save(proyecto);
    }

    @Override
    public void eliminarProyecto(UUID id) {
        repositorio.deleteById(id);
    }
}
