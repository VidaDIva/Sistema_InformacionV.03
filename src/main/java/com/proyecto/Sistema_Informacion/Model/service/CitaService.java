package com.proyecto.Sistema_Informacion.Model.service;

import java.util.List;

import com.proyecto.Sistema_Informacion.Model.entity.Cita;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoCita;

public interface CitaService {

    List<Cita> listar();

    void guardar(Cita cita);

    Cita buscarPorId(Long id);

    void eliminar(Long id);

    // 🔥 extras importantes
    List<Cita> citasPorDoctor(Long doctorId);

    List<Cita> citasPorPaciente(Long pacienteId);

    List<Cita> citasHoyDoctor(Long doctorId);

    void actualizarEstado(Long id, EstadoCita estado);

    long contar();

    long contarPorEstado(EstadoCita estado);

    List<Object[]> citasPorMes();

    List<Object[]> citasPorDoctorBI();
}
