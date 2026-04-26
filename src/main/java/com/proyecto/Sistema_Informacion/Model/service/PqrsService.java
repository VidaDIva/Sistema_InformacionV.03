package com.proyecto.Sistema_Informacion.Model.service;

import java.util.List;

import com.proyecto.Sistema_Informacion.Model.entity.PQRS;

public interface PqrsService {

    List<PQRS> listar();

    PQRS buscarPorId(Long id);

    PQRS guardar(PQRS pqrs);

    PQRS actualizar(Long id, PQRS pqrs);

    void eliminar(Long id);

    List<PQRS> buscarPorCorreo(String correo);

    List<PQRS> buscarPorEstado(String estado);

    List<PQRS> buscarPorTipoSolicitud(String tipo);

    void responderPQRS(Long id, String respuesta);

    List<PQRS> listarTodos();
}
