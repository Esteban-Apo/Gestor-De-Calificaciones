package com.backend_libertadores.libertadoresback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String contraseña;
    private String rol; 

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private Estudiante estudiante;



    public Usuario(){

    }

    // Getters y Setters
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id; 
    }

    public String getNombre(){
        return nombre; 
    }

    public void setNombre(String nombre){
        this.nombre = nombre; 
    }

    public String getEmail(){
        return email; 
    }

    public void setEmail(String email){
        this.email = email; 
    }

    public String getContraseña(){
        return contraseña;
    }

    public void setContraseña(String contraseña){
        this.contraseña = contraseña; 
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol; 
    }
 
    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }


}
