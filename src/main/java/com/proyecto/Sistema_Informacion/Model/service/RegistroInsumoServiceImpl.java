package com.proyecto.Sistema_Informacion.Model.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.Sistema_Informacion.Model.dao.RegistroInsumoDAO;
import com.proyecto.Sistema_Informacion.Model.entity.RegistroInsumo;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoInsumo;

@Service
public class RegistroInsumoServiceImpl implements RegistroInsumoService {

    private final RegistroInsumoDAO registroDAO;

    public RegistroInsumoServiceImpl(RegistroInsumoDAO registroDAO) {
        this.registroDAO = registroDAO;
    }

    @Override
    public List<RegistroInsumo> listar() {
        return registroDAO.findAll();
    }

    @Override
    public RegistroInsumo buscarPorId(Long id) {
        return registroDAO.findById(id).orElse(null);
    }

    @Override
    public RegistroInsumo guardar(RegistroInsumo registro) {

        // 🔥 AUTO: asignar fecha y hora si no vienen
        if (registro.getFechaEntrega() == null) {
            registro.setFechaEntrega(LocalDate.now());
        }

        if (registro.getHoraEntrega() == null) {
            registro.setHoraEntrega(LocalDateTime.now());
        }

        // 🔥 Estado por defecto
        if (registro.getEstado() == null) {
            registro.setEstado(EstadoInsumo.ENTREGADO);
        }

        return registroDAO.save(registro);
    }

    @Override
    public RegistroInsumo actualizar(Long id, RegistroInsumo registro) {

        RegistroInsumo existente = registroDAO.findById(id).orElse(null);

        if (existente != null) {

            existente.setNombreInsumo(registro.getNombreInsumo());
            existente.setCantidad(registro.getCantidad());
            existente.setArea(registro.getArea());
            existente.setEstado(registro.getEstado());
            existente.setObservacion(registro.getObservacion());

            // 🔥 Si pasa a DESECHADO o USADO → marcar devolución
            if (registro.getEstado() == EstadoInsumo.DESECHADO ||
                registro.getEstado() == EstadoInsumo.USADO) {

                existente.setFechaDevolucion(LocalDate.now());
                existente.setHoraDevolucion(LocalDateTime.now());
            }

            return registroDAO.save(existente);
        }

        return null;
    }

    @Override
    public void eliminar(Long id) {
        registroDAO.deleteById(id);
    }

    @Override
    public List<RegistroInsumo> buscarPorMedico(Long medicoId) {
        return registroDAO.findByMedicoId(medicoId);
    }

    @Override
    public List<RegistroInsumo> buscarPorEstado(EstadoInsumo estado) {
        return registroDAO.findByEstado(estado);
    }

    @Override
    public List<RegistroInsumo> buscarPorArea(String area) {
        return registroDAO.findByArea(area);
    }

    @Override
    public List<RegistroInsumo> noDevueltos() {
        return registroDAO.findByFechaDevolucionIsNull();
    }

    @Override
    public List<RegistroInsumo> listarTodos() {
        return registroDAO.findAll();
    }
}
