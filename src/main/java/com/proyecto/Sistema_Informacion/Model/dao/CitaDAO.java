package com.proyecto.Sistema_Informacion.Model.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proyecto.Sistema_Informacion.Model.entity.Cita;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoCita;

@Repository
public interface  CitaDAO  extends JpaRepository<Cita, Long>{
    List<Cita> findByDoctorId(Long doctorId);
    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByDoctorIdAndFecha(Long doctorId, LocalDate fecha);

    boolean existsByDoctorIdAndFechaAndHoraAndEstadoNot(
    Long doctorId,
    LocalDate fecha,
    LocalTime hora,
    EstadoCita estado
);

// ===============================
    // 🔥 CONSULTAS BI
    // ===============================

    // 🔹 Contar por estado
    long countByEstado(EstadoCita estado);

    // 🔹 Citas por mes (básico)
    @Query("""
        SELECT MONTH(c.fecha), COUNT(c)
        FROM Cita c
        GROUP BY MONTH(c.fecha)
        ORDER BY MONTH(c.fecha)
    """)
    List<Object[]> citasPorMes();

    // 🔹 Citas por mes (PRO - recomendado)
    @Query("""
        SELECT FUNCTION('DATE_FORMAT', c.fecha, '%Y-%m'), COUNT(c)
        FROM Cita c
        GROUP BY FUNCTION('DATE_FORMAT', c.fecha, '%Y-%m')
        ORDER BY FUNCTION('DATE_FORMAT', c.fecha, '%Y-%m')
    """)
    List<Object[]> citasPorMesPro();

    // 🔹 Citas por doctor (productividad)
    @Query("""
        SELECT c.doctor.nombre, COUNT(c)
        FROM Cita c
        GROUP BY c.doctor.nombre
        ORDER BY COUNT(c) DESC
    """)
    List<Object[]> citasPorDoctorBI();

}
