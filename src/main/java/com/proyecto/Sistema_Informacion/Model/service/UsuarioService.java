package com.proyecto.Sistema_Informacion.Model.service;

import java.util.List;

import com.proyecto.Sistema_Informacion.Model.entity.Crear;

public interface UsuarioService {

    List<Crear> listar();

    Crear buscarPorId(Long id);

    Crear buscarPorCorreo(String correo);

    Crear guardar(Crear usuario);

    void eliminar(Long id);

}
