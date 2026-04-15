package com.proyecto.Sistema_Informacion.Model.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cuenta")
public class Crear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 60)
    private String apellido;

    @Column(name = "celular", nullable = false, length = 15)
    private String celular;

    @Column(name = "direccion", nullable = false, length = 100)
    private String direccion;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "correo", nullable = false, length = 100)
    private String correo;

    @Column(name = "contrasena", nullable = false, length = 60)
    private String contrasena;

    @ManyToOne
    @JoinColumn(name="cargo_id", nullable=false)
    private Cargo cargo;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String imagen;

    @JsonIgnore
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<ExamenMedico> examenes;


    @OneToMany(mappedBy = "medico")
    private List<Vacuna> vacunas;

    public Crear() {
    }

    public Crear(Long id, String nombre, String apellido, String celular, String direccion, LocalDate fechaNacimiento,
            String correo, String contrasena, Cargo cargo, String imagen, List<ExamenMedico> examenes, List<Vacuna> vacunas) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.celular = celular;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.contrasena = contrasena;
        this.cargo = cargo;
        this.imagen = imagen;
        this.examenes = examenes;
        this.vacunas = vacunas;
    }

    public Long getId() {
        return id;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<ExamenMedico> getExamenes() {
        return examenes;
    }

    public void setExamenes(List<ExamenMedico> examenes) {
        this.examenes = examenes;
    }

    public List<Vacuna> getVacunas() {
        return vacunas;
    }

    public void setVacunas(List<Vacuna> vacunas) {
        this.vacunas = vacunas;
    }

    

    

    





    
}
