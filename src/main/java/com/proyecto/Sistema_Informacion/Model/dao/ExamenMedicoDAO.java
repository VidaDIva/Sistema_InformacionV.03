package com.proyecto.Sistema_Informacion.Model.dao;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Sistema_Informacion.Model.entity.ExamenMedico;

@Repository
public interface ExamenMedicoDAO extends JpaRepository<ExamenMedico, Long> {

    // 🔍 Buscar por médico
    List<ExamenMedico> findByMedicoId(Long medicoId);

    // 🔍 Exámenes vencidos
   List<ExamenMedico> findByFechaVencimientoBefore(LocalDate fecha);

}
