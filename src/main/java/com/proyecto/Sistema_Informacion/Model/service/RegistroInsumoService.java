package com.proyecto.Sistema_Informacion.Model.service;

import java.util.List;

import com.proyecto.Sistema_Informacion.Model.entity.RegistroInsumo;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoInsumo;

public interface RegistroInsumoService {

    List<RegistroInsumo> listar();

    RegistroInsumo buscarPorId(Long id);

    RegistroInsumo guardar(RegistroInsumo registro);

    RegistroInsumo actualizar(Long id, RegistroInsumo registro);

    void eliminar(Long id);

    // 🔍 Filtros
    List<RegistroInsumo> buscarPorMedico(Long medicoId);

    List<RegistroInsumo> buscarPorEstado(EstadoInsumo estado);

    List<RegistroInsumo> buscarPorArea(String area);

    List<RegistroInsumo> noDevueltos();

    List<RegistroInsumo> listarTodos();
}