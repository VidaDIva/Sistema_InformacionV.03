package com.proyecto.Sistema_Informacion.Model.entity;

import java.time.LocalDateTime;

import com.proyecto.Sistema_Informacion.Model.enums.EstadoDocumento;

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
@Table(name="documento_medico")
public class DocumentoMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_archivo", nullable = false, length = 100)
    private String nombreArchivo;

    @Column(name = "tipo_documento", nullable = false, length = 100)
    private String tipoDocumento;

    @Column(name = "nota", length = 100)
    private String nota;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida;


    @ManyToOne
    @JoinColumn(name="usuario_id")
    private Crear paciente;

   @ManyToOne
    @JoinColumn(name="cita_id")
    private Cita cita;

    @Enumerated(EnumType.STRING)
    @Column(name="estado")
    private EstadoDocumento estado;

    @Column(name = "observacion", length = 100)
    private String observacion_medico;


    public DocumentoMedico() {
    }


    public DocumentoMedico(Long id, String nombreArchivo, String tipoDocumento, String nota, LocalDateTime fechaSubida,
            Crear paciente, EstadoDocumento estado, String observacion_medico) {
        this.id = id;
        this.nombreArchivo = nombreArchivo;
        this.tipoDocumento = tipoDocumento;
        this.nota = nota;
        this.fechaSubida = fechaSubida;
        this.paciente = paciente;
        this.estado = estado;
        this.observacion_medico = observacion_medico;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getNombreArchivo() {
        return nombreArchivo;
    }


    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }


    public String getTipoDocumento() {
        return tipoDocumento;
    }


    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }


    public String getNota() {
        return nota;
    }


    public void setNota(String nota) {
        this.nota = nota;
    }


    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }


    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }


    public Crear getPaciente() {
        return paciente;
    }


    public void setPaciente(Crear paciente) {
        this.paciente = paciente;
    }


    public EstadoDocumento getEstado() {
        return estado;
    }


    public void setEstado(EstadoDocumento estado) {
        this.estado = estado;
    }


    public String getObservacion_medico() {
        return observacion_medico;
    }


    public void setObservacion_medico(String observacion_medico) {
        this.observacion_medico = observacion_medico;
    }


    public Cita getCita() {
        return cita;
    }


    public void setCita(Cita cita) {
        this.cita = cita;
    }
    
    

}
