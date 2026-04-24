package com.example.tablero.servicio.interfaces;

import com.example.tablero.entidades.dtos.entrada.TareaDtoEntrada;
import com.example.tablero.entidades.dtos.salida.TareaDtoSalida;
import com.example.tablero.entidades.dtos.salida.TareaResumidaDtoSalida;

import java.util.List;
import java.util.UUID;

public interface TareaI {
    public void guardarTarea(TareaDtoEntrada tareaDto);

    public TareaDtoSalida buscarTareaPorId(UUID idTarea);

    public void cambiarOrden(UUID tareaOrdenAnterior, UUID tareaOrdenActual);

    public List<TareaDtoSalida> buscarTareaPorTitulo(String titulo);

    public List<TareaDtoSalida> listarTarea(UUID idProyecto);

    public List<TareaResumidaDtoSalida> listarTareaResumida(UUID idProyecto);

    public void eliminarTarea(UUID idTarea);

    public void actualizarTarea(UUID idTarea, TareaDtoEntrada tareaDto);
}
