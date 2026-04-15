package com.proyecto.Sistema_Informacion.Model.service;

import java.util.List;

import com.proyecto.Sistema_Informacion.Model.entity.ExamenMedico;

public interface ExamenMedicoService {

    List<ExamenMedico> listar();

    ExamenMedico buscarPorId(Long id);

    ExamenMedico guardar(ExamenMedico examen);

    ExamenMedico actualizar(Long id, ExamenMedico examen);

    void eliminar(Long id);

    List<ExamenMedico> buscarPorMedico(Long medicoId);

    List<ExamenMedico> vencidos();
}