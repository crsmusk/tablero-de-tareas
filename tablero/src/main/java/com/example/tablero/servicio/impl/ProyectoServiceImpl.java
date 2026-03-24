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
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import org.springframework.http.HttpStatus;
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
            throw new TableroExcepcion("no se encontro al usuario con el id " + proyectoDto.getIdPerfil(),
                    HttpStatus.NOT_FOUND);
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
                repositorio.findById(id).orElseThrow(
                        () -> new TableroExcepcion("No se encontró el proyecto con id " + id, HttpStatus.NOT_FOUND)),
                repositorio.contarTareasCompletadas(id),
                repositorio.contarTareasPendientes(id),
                repositorio.contarTareasEnProgreso(id),
                repositorio.contarTareasEnRevision(id));
    }

    @Override
    public void actualizarProyecto(UUID id, ProyectoDtoEntrada proyectoDto) {
        ProyectoEntity proyecto = repositorio.findById(id)
                .orElseThrow(
                        () -> new TableroExcepcion("No se encontró el proyecto con id " + id, HttpStatus.NOT_FOUND));
        if (proyectoDto.getNombreProyecto() != null && !proyectoDto.getNombreProyecto().isEmpty()) {
            proyecto.setNombreProyecto(proyectoDto.getNombreProyecto());
        }
        if (proyectoDto.getCorreoCliente() != null && !proyectoDto.getCorreoCliente().isEmpty()) {
            proyecto.setCorreoCliente(proyectoDto.getCorreoCliente());
        }
        if (proyectoDto.getNombreCliente() != null && !proyectoDto.getNombreCliente().isEmpty()) {
            proyecto.setNombreCliente(proyectoDto.getNombreCliente());
        }
        repositorio.save(proyecto);
    }

    @Override
    public void eliminarProyecto(UUID id) {
        ProyectoEntity proyecto = repositorio.findById(id)
                .orElseThrow(
                        () -> new TableroExcepcion("No se encontró el proyecto con id " + id, HttpStatus.NOT_FOUND));
        repositorio.delete(proyecto);
    }
}
