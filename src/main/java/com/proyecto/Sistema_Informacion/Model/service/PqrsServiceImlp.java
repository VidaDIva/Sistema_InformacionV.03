package com.proyecto.Sistema_Informacion.Model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Sistema_Informacion.Model.dao.PqrsDAO;
import com.proyecto.Sistema_Informacion.Model.entity.PQRS;

@Service
public class PqrsServiceImlp implements PqrsService {

    @Autowired
    private PqrsDAO pqrsDAO;

    @Override
    public List<PQRS> listar() {
        return pqrsDAO.findAll();
    }

    @Override
    public PQRS buscarPorId(Long id) {
        return pqrsDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("PQRS no encontrado con id: " + id));
    }

    @Override
    public PQRS guardar(PQRS pqrs) {
        pqrs.setEstado("Pendiente");
        return pqrsDAO.save(pqrs);
    }

    @Override
    public PQRS actualizar(Long id, PQRS pqrs) {

        PQRS existente = pqrsDAO.findById(id).orElse(null);

        if (existente != null) {

            existente.setTipoUsuario(pqrs.getTipoUsuario());
            existente.setNombre(pqrs.getNombre());
            existente.setTipoDocumento(pqrs.getTipoDocumento());
            existente.setNumeroDocumento(pqrs.getNumeroDocumento());
            existente.setTelefono(pqrs.getTelefono());
            existente.setCorreo(pqrs.getCorreo());
            existente.setAreaHospital(pqrs.getAreaHospital());
            existente.setFechaIncidente(pqrs.getFechaIncidente());
            existente.setTipoSolicitud(pqrs.getTipoSolicitud());
            existente.setDescripcion(pqrs.getDescripcion());
            existente.setEstado(pqrs.getEstado());
            existente.setUbicacion(pqrs.getUbicacion());
            existente.setNivelGravedad(pqrs.getNivelGravedad());
            return pqrsDAO.save(existente);
        }

        return null;
    }

    @Override
    public void eliminar(Long id) {
        if (!pqrsDAO.existsById(id)) {
            throw new RuntimeException("PQRS no encontrado con id: " + id);
        }
        pqrsDAO.deleteById(id);
    }

    @Override
    public List<PQRS> buscarPorCorreo(String correo) {
        return pqrsDAO.findByCorreo(correo);
    }

    @Override
    public List<PQRS> buscarPorEstado(String estado) {
        return pqrsDAO.findByEstado(estado);
    }

    @Override
    public List<PQRS> buscarPorTipoSolicitud(String tipo) {
        return pqrsDAO.findByTipoSolicitud(tipo);
    }

    @Override
    public void responderPQRS(Long id, String respuesta) {
        PQRS pqrs = pqrsDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("PQRS no encontrado con id: " + id));
        pqrs.setRespuesta(respuesta);
        pqrs.setEstado("Cerrado");
        pqrsDAO.save(pqrs);
    }

    @Override
    public List<PQRS> listarTodos() {
        return pqrsDAO.findAll();
    }

}
