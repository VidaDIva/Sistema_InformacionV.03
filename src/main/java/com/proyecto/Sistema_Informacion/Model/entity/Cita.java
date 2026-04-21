package com.proyecto.Sistema_Informacion.Model.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.proyecto.Sistema_Informacion.Model.enums.EstadoCita;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 PACIENTE
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Crear paciente;

    // 🔗 MÉDICO
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Crear doctor;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name= "motivo",length = 255)
    private String motivo; 

    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    public Cita() {
    }

    public Cita(Long id, Crear paciente, Crear doctor, LocalDate fecha, LocalTime hora, String motivo,
            EstadoCita estado) {
        this.id = id;
        this.paciente = paciente;
        this.doctor = doctor;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Crear getPaciente() {
        return paciente;
    }

    public void setPaciente(Crear paciente) {
        this.paciente = paciente;
    }

    public Crear getDoctor() {
        return doctor;
    }

    public void setDoctor(Crear doctor) {
        this.doctor = doctor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    

}
