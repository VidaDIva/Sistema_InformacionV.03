package com.proyecto.Sistema_Informacion.Model.service;

import java.util.List;

import com.proyecto.Sistema_Informacion.Model.entity.Vacuna;

public interface VacunaService {

    List<Vacuna> listar();

    Vacuna buscarPorId(Long id);

    Vacuna guardar(Vacuna vacuna);

    Vacuna actualizar(Long id, Vacuna vacuna);

    void eliminar(Long id);

    List<Vacuna> buscarPorMedico(Long medicoId);

    List<Vacuna> proximas();

    List<Vacuna> listarTodos();
}