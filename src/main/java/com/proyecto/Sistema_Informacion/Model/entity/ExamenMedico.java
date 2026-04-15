package com.proyecto.Sistema_Informacion.Model.entity;

import java.time.LocalDate;

import com.proyecto.Sistema_Informacion.Model.enums.EstadoExamen;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "examen_medico")
public class ExamenMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Relación con médico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private Crear medico;

    @Column(name = "tipo_examen", nullable = false)
    private String tipoExamen;

    @Column(name = "fecha_realizacion", nullable = false)
    private LocalDate fechaRealizacion;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoExamen estado;

    @Column(name = "observaciones", nullable = false, length = 200)
    private String observaciones;

    public ExamenMedico() {}

    public ExamenMedico(Long id, Crear medico, String tipoExamen,
                        LocalDate fechaRealizacion, LocalDate fechaVencimiento,
                        EstadoExamen estado, String observaciones) {

        this.id = id;
        this.medico = medico;
        this.tipoExamen = tipoExamen;
        this.fechaRealizacion = fechaRealizacion;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    // ======================
    // GETTERS Y SETTERS
    // ======================

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

    public String getTipoExamen() {
        return tipoExamen;
    }

    public void setTipoExamen(String tipoExamen) {
        this.tipoExamen = tipoExamen;
    }

    public LocalDate getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(LocalDate fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public EstadoExamen getEstado() {
        return estado;
    }

    public void setEstado(EstadoExamen estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}