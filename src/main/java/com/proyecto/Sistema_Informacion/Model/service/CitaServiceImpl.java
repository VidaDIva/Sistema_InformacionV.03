package com.proyecto.Sistema_Informacion.Model.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Sistema_Informacion.Model.dao.CitaDAO;
import com.proyecto.Sistema_Informacion.Model.entity.Cita;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoCita;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaDAO citaDAO;

    @Override
    public List<Cita> listar() {
        return citaDAO.findAll();
    }

    @Override
public void guardar(Cita cita) {

    // 🔥 VALIDACIONES BÁSICAS
    if (cita.getPaciente() == null || cita.getDoctor() == null) {
        throw new RuntimeException("Paciente o Doctor no pueden ser nulos");
    }

    if (cita.getFecha() == null || cita.getHora() == null) {
        throw new RuntimeException("Fecha y hora son obligatorias");
    }

    // 🔥 VALIDAR FECHA PASADA (PRO)
    if (cita.getFecha().isBefore(LocalDate.now())) {
        throw new RuntimeException("No puedes agendar en fechas pasadas");
    }

    // 🔥 ESTADO POR DEFECTO
    if (cita.getEstado() == null) {
        cita.setEstado(EstadoCita.PENDIENTE);
    }

    // 🔥 VALIDAR DUPLICADO
    boolean existe = citaDAO.existsByDoctorIdAndFechaAndHoraAndEstadoNot(
            cita.getDoctor().getId(),
            cita.getFecha(),
            cita.getHora(),
            EstadoCita.CANCELADA
    );

    if (existe) {
        throw new RuntimeException("El doctor ya tiene una cita en ese horario");
    }

    // 🔥 GUARDAR
    citaDAO.save(cita);
}



    @Override
    public Cita buscarPorId(Long id) {
        return citaDAO.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        citaDAO.deleteById(id);
    }

    // 🔥 citas por doctor
    @Override
    public List<Cita> citasPorDoctor(Long doctorId) {
        return citaDAO.findByDoctorId(doctorId);
    }

    // 🔥 citas por paciente
    @Override
    public List<Cita> citasPorPaciente(Long pacienteId) {
        return citaDAO.findByPacienteId(pacienteId);
    }

    // 🔥 citas de hoy (CLAVE para tu IA 🔥)
    @Override
    public List<Cita> citasHoyDoctor(Long doctorId) {
        return citaDAO.findByDoctorIdAndFecha(doctorId, LocalDate.now());
    }

    @Override
    public void actualizarEstado(Long id, EstadoCita estado) {
        Cita cita = citaDAO.findById(id)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        cita.setEstado(estado);

        citaDAO.save(cita); // 🔥 NO valida duplicados
    }

    @Override
    public long contar() {
        return citaDAO.count();
    }

    @Override
    public long contarPorEstado(EstadoCita estado) {
        return citaDAO.countByEstado(estado);
    }

    @Override
    public List<Object[]> citasPorMes() {
        return citaDAO.citasPorMes();
    }

    @Override
    public List<Object[]> citasPorDoctorBI() {
        return citaDAO.citasPorDoctorBI();
    }

    @Override
    public List<Cita> listarTodas() {
        return citaDAO.findAll();
    }

}
