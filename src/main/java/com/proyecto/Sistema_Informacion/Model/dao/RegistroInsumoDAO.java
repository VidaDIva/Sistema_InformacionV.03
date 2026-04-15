package com.proyecto.Sistema_Informacion.Model.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Sistema_Informacion.Model.entity.RegistroInsumo;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoInsumo;

@Repository
public interface RegistroInsumoDAO extends JpaRepository<RegistroInsumo, Long> {

    // 🔍 Buscar por médico
    List<RegistroInsumo> findByMedicoId(Long medicoId);

    // 🔍 Buscar por estado (ENUM 🔥)
    List<RegistroInsumo> findByEstado(EstadoInsumo estado);

    // 🔍 Buscar por área
    List<RegistroInsumo> findByArea(String area);

    // 🔍 Insumos entregados en una fecha
    List<RegistroInsumo> findByFechaEntrega(LocalDate fecha);

    // 🔍 Insumos no devueltos (fechaDevolucion null)
    List<RegistroInsumo> findByFechaDevolucionIsNull();
}