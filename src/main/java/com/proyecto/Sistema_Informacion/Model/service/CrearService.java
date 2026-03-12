package com.proyecto.Sistema_Informacion.Model.service;

import java.util.List;

import com.proyecto.Sistema_Informacion.Model.entity.Crear;

public interface CrearService {

    List<Crear> listar();

    Crear buscarPorId(Long id);

    Crear guardar(Crear crear);

    Crear actualizar(Long id, Crear crear);

    void eliminar(Long id);

    Crear buscarPorCorreo(String correo);

}
