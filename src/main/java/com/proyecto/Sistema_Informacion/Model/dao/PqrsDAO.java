package com.proyecto.Sistema_Informacion.Model.dao;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.Sistema_Informacion.Model.entity.PQRS;

@Repository
public interface  PqrsDAO extends JpaRepository<PQRS, Long> {

    List<PQRS> findByEstado(String estado);

   
    List<PQRS> findByTipoSolicitud(String tipoSolicitud);

    List<PQRS> findByCorreo(String correo);

    List<PQRS> findByAreaHospital(String areaHospital);
    
}
