package com.proyecto.Sistema_Informacion.Model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Sistema_Informacion.Model.entity.DocumentoMedico;
import com.proyecto.Sistema_Informacion.Model.enums.EstadoDocumento;

@Repository
public interface DocumentoMedicoDAO extends JpaRepository<DocumentoMedico, Long>{

    //Traer todos los documentos de un paciente por id
    List<DocumentoMedico> findByPacienteId(Long id);

    List<DocumentoMedico> findByEstado(EstadoDocumento estado);

    List<DocumentoMedico> findByCita_Doctor_Id(Long id);
}
