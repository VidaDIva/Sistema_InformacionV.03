package com.proyecto.Sistema_Informacion.Model.entity;

import java.time.LocalDate;

import com.proyecto.Sistema_Informacion.Model.enums.EstadoVacuna;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vacuna")
public class Vacuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Crear medico;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "fecha_aplicacion", nullable = false)
    private LocalDate fechaAplicacion;

    @Column(name = "fecha_refuerzo", nullable = false)
    private LocalDate fechaRefuerzo;

      @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVacuna estado;


    public Vacuna() {
    }

    public Vacuna(Long id, Crear medico, String nombre, LocalDate fechaAplicacion, LocalDate fechaRefuerzo, EstadoVacuna estado) {
        this.id = id;
        this.medico = medico;
        this.nombre = nombre;
        this.fechaAplicacion = fechaAplicacion;
        this.fechaRefuerzo = fechaRefuerzo;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Crear getMedico() {
        return medico;
    }

    public void setMedico(Crear medico) {
        this.medico = medico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(LocalDate fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public LocalDate getFechaRefuerzo() {
        return fechaRefuerzo;
    }

    public void setFechaRefuerzo(LocalDate fechaRefuerzo) {
        this.fechaRefuerzo = fechaRefuerzo;
    }

    public EstadoVacuna getEstado() {
        return estado;
    }

    public void setEstado(EstadoVacuna estado) {
        this.estado = estado;
    }
    

    

}