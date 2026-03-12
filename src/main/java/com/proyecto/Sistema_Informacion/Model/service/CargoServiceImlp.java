package com.proyecto.Sistema_Informacion.Model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Sistema_Informacion.Model.dao.CargoDAO;
import com.proyecto.Sistema_Informacion.Model.entity.Cargo;

@Service
public class CargoServiceImlp implements CargoService {
    @Autowired
    private CargoDAO cargoDAO;

    @Override
    public List<Cargo> listar() {
        return cargoDAO.findAll();
    }

    @Override
    public Cargo buscarPorId(Long id) {
        return cargoDAO.findById(id).orElse(null);
    }

    @Override
    public Cargo guardar(Cargo cargo) {
        return cargoDAO.save(cargo);
    }

    @Override
    public void eliminar(Long id) {
        cargoDAO.deleteById(id);
    }
}
