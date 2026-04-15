package com.proyecto.Sistema_Informacion.Model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Sistema_Informacion.Model.entity.Cargo;

@Repository
public interface CargoDAO extends JpaRepository<Cargo, Long> {
    
}
