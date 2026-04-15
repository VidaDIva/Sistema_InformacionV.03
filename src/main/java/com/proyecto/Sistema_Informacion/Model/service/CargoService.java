package com.proyecto.Sistema_Informacion.Model.service;

import java.util.List;

import com.proyecto.Sistema_Informacion.Model.entity.Cargo;

public interface CargoService {
    List<Cargo> listar();
    Cargo buscarPorId(Long id);
    Cargo guardar(Cargo cargo);
    void eliminar(Long id);
}
