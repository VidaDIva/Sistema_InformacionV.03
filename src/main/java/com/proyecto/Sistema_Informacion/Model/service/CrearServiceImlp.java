package com.proyecto.Sistema_Informacion.Model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Sistema_Informacion.Model.dao.CrearDAO;
import com.proyecto.Sistema_Informacion.Model.entity.Cargo;
import com.proyecto.Sistema_Informacion.Model.entity.Crear;

@Service
public class CrearServiceImlp implements CrearService {

    @Autowired
    private CrearDAO crearDAO;

    @Autowired
    private CargoService cargoService;

    @Override
    public Crear buscarPorCorreo(String correo) {
        return crearDAO.findByCorreo(correo);
    }

    @Override
    public List<Crear> listar() {
        return crearDAO.findAll();
    }

    @Override
    public Crear buscarPorId(Long id) {
        return crearDAO.findById(id).orElse(null);
    }

    @Override
    public Crear guardar(Crear crear) {
        return crearDAO.save(crear);
    }

    @Override
    public Crear actualizar(Long id, Crear crear) {
        Crear existente = crearDAO.findById(id).orElse(null);
        if (existente != null) {
            existente.setNombre(crear.getNombre());
            existente.setApellido(crear.getApellido());
            existente.setCorreo(crear.getCorreo());
            existente.setCelular(crear.getCelular());
            existente.setDireccion(crear.getDireccion());
            return crearDAO.save(existente);
        }
        return null;
    }

    @Override
    public void eliminar(Long id) {
        crearDAO.deleteById(id);
    }

    @Override
    public List<Crear> listarMedicos() {

        Cargo cargoMedico = cargoService.listar()
            .stream()
            .filter(c -> c.getCargo().equalsIgnoreCase("DOCTOR"))
            .findFirst()
            .orElse(null);

        return crearDAO.findByCargo(cargoMedico);
    }

    @Override
    public List<Crear> listarPacientes() {

        Cargo cargoPaciente = cargoService.listar()
            .stream()
            .filter(c -> c.getCargo().equalsIgnoreCase("PACIENTE"))
            .findFirst()
            .orElse(null);

        return crearDAO.findByCargo(cargoPaciente);
    }

     @Override
    public List<Crear> listarTodos() {
        return crearDAO.findAll();
    }
}
