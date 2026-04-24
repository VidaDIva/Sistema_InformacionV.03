package com.proyecto.Sistema_Informacion.Model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Sistema_Informacion.Model.dao.DocumentoMedicoDAO;
import com.proyecto.Sistema_Informacion.Model.entity.DocumentoMedico;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoDocumento;

@Service
public class DocumentoMedicoServiceImpl
implements DocumentoMedicoService {

    @Autowired
    private DocumentoMedicoDAO documentoDAO;


    @Override
    public void guardar(DocumentoMedico documento) {
        documentoDAO.save(documento);
    }


    @Override
    public List<DocumentoMedico> listarPorPaciente(Long idPaciente) {
        return documentoDAO.findByPacienteId(idPaciente);
    }


    @Override
    public DocumentoMedico buscarPorId(Long id) {
        return documentoDAO.findById(id).orElse(null);
    }


    @Override
    public void eliminar(Long id) {
        documentoDAO.deleteById(id);
    }

    @Override
    public List<DocumentoMedico> listarPendientes(){
    return documentoDAO.findByEstado(
        EstadoDocumento.PENDIENTE
    );
    }


     @Override
    public List<DocumentoMedico> buscarPorDoctor(Long doctorId) {
        return documentoDAO.findByCita_Doctor_Id(doctorId);
    }
}