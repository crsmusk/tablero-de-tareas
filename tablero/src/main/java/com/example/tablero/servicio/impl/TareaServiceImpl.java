package com.example.tablero.servicio.impl;

import com.example.tablero.entidades.dtos.entrada.EntregableDtoEntrada;
import com.example.tablero.entidades.dtos.entrada.TareaDtoEntrada;
import com.example.tablero.entidades.dtos.salida.TareaDtoSalida;
import com.example.tablero.entidades.entidades.ProyectoEntity;
import com.example.tablero.entidades.entidades.TareaEntity;
import com.example.tablero.entidades.entidades.enums.EstadosTarea;
import com.example.tablero.excepciones.excepcion.TableroExcepcion;
import com.example.tablero.mapper.TareaMapper;
import com.example.tablero.repositorio.ProyectoRepositorio;
import com.example.tablero.repositorio.TareaRepositorio;
import com.example.tablero.servicio.interfaces.TareaI;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TareaServiceImpl implements TareaI {

    private TareaRepositorio repositorio;
    private TareaMapper mapper;
    private ProyectoRepositorio repositorioProyecto;
    private EntregableServiceImpl entregableService;

    public TareaServiceImpl(TareaRepositorio repositorio, TareaMapper mapper, ProyectoRepositorio repositorioProyecto,
            EntregableServiceImpl entregableService) {
        this.repositorio = repositorio;
        this.mapper = mapper;
        this.repositorioProyecto = repositorioProyecto;
        this.entregableService = entregableService;
    }

    @Override
    public void guardarTarea(TareaDtoEntrada tareaDto) {
        if (tareaDto.getIdProyectoAsociado() == null
                || !repositorioProyecto.existsById(UUID.fromString(tareaDto.getIdProyectoAsociado()))) {
            throw new TableroExcepcion("No se encontró el proyecto con el id " + tareaDto.getIdProyectoAsociado(),
                    HttpStatus.NOT_FOUND);
        }
        ProyectoEntity proyecto = new ProyectoEntity();
        proyecto.setId(UUID.fromString(tareaDto.getIdProyectoAsociado()));
        TareaEntity tarea = new TareaEntity();
        tarea.setDescripcion(tareaDto.getDescripcion() != null ? tareaDto.getDescripcion() : "");
        tarea.setClienteAccion(tareaDto.getClienteAccion() != null ? tareaDto.getClienteAccion() : "");
        tarea.setEstado(tareaDto.getEstado() != null ? EstadosTarea.valueOf(tareaDto.getEstado().toUpperCase())
                : EstadosTarea.PENDIENTE);
        tarea.setTitulo(tareaDto.getTitulo() != null ? tareaDto.getTitulo() : "");
        tarea.setProyectoAsociado(proyecto);
        tarea.setPosicion(tareaDto.getPosicion());
        repositorio.save(tarea);
        if (tareaDto.getEntregables() != null) {
            for (EntregableDtoEntrada entregableDto : tareaDto.getEntregables()) {
                entregableDto.setIdTarea(tarea.getId().toString());
                entregableService.guardarEntregable(entregableDto);
            }
        }
    }

    @Override
    public TareaDtoSalida buscarTareaPorId(UUID idTarea) {
        return mapper.tareaM(
                repositorio.findById(idTarea)
                        .orElseThrow(() -> new TableroExcepcion("No se encontró la tarea con el id " + idTarea,
                                HttpStatus.NOT_FOUND)));
    }

    @Transactional
    @Override
    public void cambiarOrden(UUID tareaOrdenAnterior, UUID tareaOrdenActual) {
        int ordenOld = 0;
        TareaEntity ordenViejo = repositorio.findById(tareaOrdenAnterior)
                .orElseThrow(() -> new TableroExcepcion("No se encontró la tarea con el id " + tareaOrdenAnterior,
                        HttpStatus.NOT_FOUND));
        ordenOld = ordenViejo.getPosicion();
        TareaEntity ordenActual = repositorio.findById(tareaOrdenActual)
                .orElseThrow(() -> new TableroExcepcion("No se encontró la tarea con el id " + tareaOrdenActual,
                        HttpStatus.NOT_FOUND));
        ordenViejo.setPosicion(ordenActual.getPosicion());
        ordenActual.setPosicion(ordenOld);
        repositorio.save(ordenActual);
        repositorio.save(ordenViejo);
    }

    @Override
    public List<TareaDtoSalida> buscarTareaPorTitulo(String titulo) {
        return mapper.tareasM(repositorio.findByTituloContainingIgnoreCase(titulo));
    }

    @Override
    public List<TareaDtoSalida> listarTarea() {
        return mapper.tareasM(repositorio.findAll());
    }

    @Override
    public void eliminarTarea(UUID idTarea) {
        TareaEntity tarea = repositorio.findById(idTarea)
                .orElseThrow(() -> new TableroExcepcion("No se encontró la tarea con el id " + idTarea,
                        HttpStatus.NOT_FOUND));
        repositorio.delete(tarea);
    }

    @Override
    public void actualizarTarea(UUID idTarea, TareaDtoEntrada tareaDto) {
        TareaEntity tarea = repositorio.findById(idTarea)
                .orElseThrow(() -> new TableroExcepcion("No se encontró la tarea con el id " + idTarea,
                        HttpStatus.NOT_FOUND));

        if (tareaDto.getDescripcion() != null && !tareaDto.getDescripcion().isEmpty()) {
            tarea.setDescripcion(tareaDto.getDescripcion());
        }
        if (tareaDto.getEstado() != null && !tareaDto.getEstado().isEmpty()) {
            tarea.setEstado(EstadosTarea.valueOf(tareaDto.getEstado().toUpperCase()));
        }

        if (tareaDto.getClienteAccion() != null && !tareaDto.getClienteAccion().isEmpty()) {
            tarea.setClienteAccion(tareaDto.getClienteAccion());
        }

        if (tareaDto.getTitulo() != null && !tareaDto.getTitulo().isEmpty()) {
            tarea.setTitulo(tareaDto.getTitulo());
        }

        repositorio.save(tarea);
    }

}
