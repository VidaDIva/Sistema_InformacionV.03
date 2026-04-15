package com.proyecto.Sistema_Informacion.Model.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.Sistema_Informacion.Model.dao.VacunaDAO;
import com.proyecto.Sistema_Informacion.Model.entity.Vacuna;

@Service
public class VacunaServiceImpl implements VacunaService {

    private final VacunaDAO vacunaDAO;

    public VacunaServiceImpl(VacunaDAO vacunaDAO) {
        this.vacunaDAO = vacunaDAO;
    }

    @Override
    public List<Vacuna> listar() {
        return vacunaDAO.findAll();
    }

    @Override
    public Vacuna buscarPorId(Long id) {
        return vacunaDAO.findById(id).orElse(null);
    }

    @Override
    public Vacuna guardar(Vacuna vacuna) {
        return vacunaDAO.save(vacuna);
    }

    @Override
    public Vacuna actualizar(Long id, Vacuna vacuna) {

        Vacuna existente = vacunaDAO.findById(id).orElse(null);

        if (existente != null) {
            existente.setNombre(vacuna.getNombre());
            existente.setFechaAplicacion(vacuna.getFechaAplicacion());
            existente.setFechaRefuerzo(vacuna.getFechaRefuerzo());

            return vacunaDAO.save(existente);
        }

        return null;
    }

    @Override
    public void eliminar(Long id) {
        vacunaDAO.deleteById(id);
    }

    @Override
    public List<Vacuna> buscarPorMedico(Long medicoId) {
        return vacunaDAO.findByMedicoId(medicoId);
    }

    @Override
    public List<Vacuna> proximas() {
        return vacunaDAO.findByFechaRefuerzoBefore(LocalDate.now());
    }
}