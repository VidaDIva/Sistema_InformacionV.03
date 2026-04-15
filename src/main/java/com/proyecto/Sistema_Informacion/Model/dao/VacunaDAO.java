package com.proyecto.Sistema_Informacion.Model.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Sistema_Informacion.Model.entity.Vacuna;

@Repository
public interface VacunaDAO extends JpaRepository<Vacuna, Long> {

    // 🔍 Buscar por médico
    List<Vacuna> findByMedicoId(Long medicoId);

    // 🔍 Vacunas vencidas o próximas
   List<Vacuna> findByFechaRefuerzoBefore(LocalDate fecha);

}