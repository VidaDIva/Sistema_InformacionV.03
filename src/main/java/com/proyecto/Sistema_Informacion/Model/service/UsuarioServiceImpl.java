package com.proyecto.Sistema_Informacion.Model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.Sistema_Informacion.Model.dao.UsuarioDAO;
import com.proyecto.Sistema_Informacion.Model.entity.Crear;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Override
    public List<Crear> listar() {
        return usuarioDAO.findAll();
    }

    @Override
    public Crear buscarPorId(Long id) {
        return usuarioDAO.findById(id).orElse(null);
    }

    @Override
    public Crear buscarPorCorreo(String correo) {
        return usuarioDAO.findByCorreo(correo);
    }

    @Override
    public Crear guardar(Crear usuario) {
        return usuarioDAO.save(usuario);
    }

    @Override
    public void eliminar(Long id) {
        usuarioDAO.deleteById(id);
    }
}
