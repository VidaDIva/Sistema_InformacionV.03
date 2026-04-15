package com.proyecto.Sistema_Informacion.Model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.proyecto.Sistema_Informacion.Model.enums.EstadoInsumo;

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
@Table(name = "registro_insumo")
public class RegistroInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👨‍⚕️ Médico que recibe los insumos
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Crear medico;

    // 🧴 Nombre del insumo
    @Column(nullable = false)
    private String nombreInsumo;

    // 🔢 Cantidad entregada
    @Column(nullable = false)
    private int cantidad;

    // 📅 Fecha de entrega
    @Column(nullable = false)
    private LocalDate fechaEntrega;

    // ⏰ Hora exacta (más preciso)
    private LocalDateTime horaEntrega;

    // 🗑️ Fecha de devolución o desecho
    private LocalDate fechaDevolucion;

    private LocalDateTime horaDevolucion;

    // 📍 Área hospitalaria
    private String area;

    // 📊 Estado del insumo
    // ENTREGADO - USADO - DESECHADO
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoInsumo estado;

    // 📝 Observaciones
    @Column(length = 200)
    private String observacion;

    public RegistroInsumo() {
    }

    // Constructor
    public RegistroInsumo(Long id, Crear medico, String nombreInsumo, int cantidad,
            LocalDate fechaEntrega, LocalDateTime horaEntrega,
            LocalDate fechaDevolucion, LocalDateTime horaDevolucion,
            String area, EstadoInsumo estado, String observacion) {

        this.id = id;
        this.medico = medico;
        this.nombreInsumo = nombreInsumo;
        this.cantidad = cantidad;
        this.fechaEntrega = fechaEntrega;
        this.horaEntrega = horaEntrega;
        this.fechaDevolucion = fechaDevolucion;
        this.horaDevolucion = horaDevolucion;
        this.area = area;
        this.estado = estado;
        this.observacion = observacion;
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

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public LocalDateTime getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(LocalDateTime horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public LocalDateTime getHoraDevolucion() {
        return horaDevolucion;
    }

    public void setHoraDevolucion(LocalDateTime horaDevolucion) {
        this.horaDevolucion = horaDevolucion;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public EstadoInsumo getEstado() {
        return estado;
    }

    public void setEstado(EstadoInsumo estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    // GETTERS Y SETTERS ↓↓↓

    
}