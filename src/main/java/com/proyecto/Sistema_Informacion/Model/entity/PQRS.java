package com.proyecto.Sistema_Informacion.Model.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pqrs")
public class PQRS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_usuario", nullable = false, length = 20)
    private String tipoUsuario;

   
    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String tipoDocumento;

    @Column(name = "numero_documento", nullable = false, length = 20)
    private String numeroDocumento;

  
    @Column(name = "telefono", nullable = false, length = 15)
    private String telefono;

    @Column(name = "correo", nullable = false, length = 60)
    private String correo;

    
    @Column(name = "area_hospital", nullable = false, length = 30)
    private String areaHospital;

    @Column(name = "fecha_incidente", nullable = false)
    private LocalDate fechaIncidente;

    @Column(name = "tipo_solicitud", nullable = false, length = 20)
    private String tipoSolicitud;

    @Column(name = "descripcion", nullable = false, length = 300)
    private String descripcion;

    
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

  
    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    @Column(name = "nivel_gravedad", length = 20)
    private String nivelGravedad;

  
    public PQRS() {
    }

    public PQRS(Long id, String tipoUsuario, String nombre, String tipoDocumento, String numeroDocumento,
                String telefono, String correo, String areaHospital, LocalDate fechaIncidente,
                String tipoSolicitud, String descripcion, String estado,
                String ubicacion, String nivelGravedad) {

        this.id = id;
        this.tipoUsuario = tipoUsuario;
        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.telefono = telefono;
        this.correo = correo;
        this.areaHospital = areaHospital;
        this.fechaIncidente = fechaIncidente;
        this.tipoSolicitud = tipoSolicitud;
        this.descripcion = descripcion;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.nivelGravedad = nivelGravedad;
    }

    

    public Long getId() {
        return id;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getAreaHospital() {
        return areaHospital;
    }

    public void setAreaHospital(String areaHospital) {
        this.areaHospital = areaHospital;
    }

    public LocalDate getFechaIncidente() {
        return fechaIncidente;
    }

    public void setFechaIncidente(LocalDate fechaIncidente) {
        this.fechaIncidente = fechaIncidente;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getNivelGravedad() {
        return nivelGravedad;
    }

    public void setNivelGravedad(String nivelGravedad) {
        this.nivelGravedad = nivelGravedad;
    }
}