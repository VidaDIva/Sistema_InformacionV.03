package com.proyecto.Sistema_Informacion.Model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Sistema_Informacion.Model.entity.ChatMensaje;

@Repository
public interface ChatDAO extends JpaRepository<ChatMensaje, Long> {

    
    List<ChatMensaje> findByMedicoIdOrderByFechaAsc(Long medicoId);
}
