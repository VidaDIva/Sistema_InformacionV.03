package com.proyecto.Sistema_Informacion.Model.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.Sistema_Informacion.Model.dao.ExamenMedicoDAO;
import com.proyecto.Sistema_Informacion.Model.entity.ExamenMedico;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoExamen;

@Service
public class ExamenMedicoServiceImpl implements ExamenMedicoService {

    private final ExamenMedicoDAO examenDAO;

    public ExamenMedicoServiceImpl(ExamenMedicoDAO examenDAO) {
        this.examenDAO = examenDAO;
    }

    // 📌 Listar todos
    @Override
    public List<ExamenMedico> listar() {
        return examenDAO.findAll();
    }

    // 📌 Buscar por ID
    @Override
    public ExamenMedico buscarPorId(Long id) {
        return examenDAO.findById(id).orElse(null);
    }

    // 📌 Guardar con lógica automática de estado
    @Override
    public ExamenMedico guardar(ExamenMedico examen) {

        if (examen.getFechaVencimiento() != null) {

            if (examen.getFechaVencimiento().isBefore(LocalDate.now())) {
                examen.setEstado(EstadoExamen.VENCIDO);
            } else {
                examen.setEstado(EstadoExamen.VIGENTE);
            }
        }

        return examenDAO.save(examen);
    }

    // 📌 Actualizar
    @Override
    public ExamenMedico actualizar(Long id, ExamenMedico examen) {

        ExamenMedico existente = examenDAO.findById(id).orElse(null);

        if (existente != null) {

            existente.setTipoExamen(examen.getTipoExamen());
            existente.setFechaRealizacion(examen.getFechaRealizacion());
            existente.setFechaVencimiento(examen.getFechaVencimiento());
            existente.setObservaciones(examen.getObservaciones());

            // 🔥 Recalcular estado automáticamente
            if (examen.getFechaVencimiento() != null) {
                if (examen.getFechaVencimiento().isBefore(LocalDate.now())) {
                    existente.setEstado(EstadoExamen.VENCIDO);
                } else {
                    existente.setEstado(EstadoExamen.VIGENTE);
                }
            }

            return examenDAO.save(existente);
        }

        return null;
    }

    // 📌 Eliminar
    @Override
    public void eliminar(Long id) {
        examenDAO.deleteById(id);
    }

    // 📌 Buscar por médico
    @Override
    public List<ExamenMedico> buscarPorMedico(Long medicoId) {
        return examenDAO.findByMedicoId(medicoId);
    }

    // 📌 Exámenes vencidos
    @Override
    public List<ExamenMedico> vencidos() {
        return examenDAO.findByFechaVencimientoBefore(LocalDate.now());
    }

     @Override
    public List<ExamenMedico> listarTodos() {
        return examenDAO.findAll();
    }
    
}