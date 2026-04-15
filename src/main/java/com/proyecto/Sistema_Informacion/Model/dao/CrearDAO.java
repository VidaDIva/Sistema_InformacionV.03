package com.proyecto.Sistema_Informacion.Model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Sistema_Informacion.Model.entity.Cargo;
import com.proyecto.Sistema_Informacion.Model.entity.Crear;

@Repository
public interface CrearDAO extends JpaRepository<Crear, Long> {
    Crear findByCorreo(String correo);

    List<Crear> findByCargo(Cargo cargo);
    
}
