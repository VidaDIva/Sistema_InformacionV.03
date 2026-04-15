package com.proyecto.Sistema_Informacion.Model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.Sistema_Informacion.Model.entity.Crear;

public interface UsuarioDAO extends JpaRepository<Crear, Long> {

    Crear findByCorreo(String correo);

}
