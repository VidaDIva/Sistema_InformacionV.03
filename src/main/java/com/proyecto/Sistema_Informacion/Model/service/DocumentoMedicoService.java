package com.proyecto.Sistema_Informacion.Model.service;

import java.util.List;

import com.proyecto.Sistema_Informacion.Model.entity.DocumentoMedico;

public interface DocumentoMedicoService {

    void guardar(DocumentoMedico documento);

    List<DocumentoMedico> listarPorPaciente(Long idPaciente);

    DocumentoMedico buscarPorId(Long id);

    void eliminar(Long id);
    
    List<DocumentoMedico> listarPendientes();


    List<DocumentoMedico> buscarPorDoctor(Long id);

    List<DocumentoMedico> listarTodos();
}
