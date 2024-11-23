package com.backend_libertadores.libertadoresback.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;

    @Column(unique = true) // Restringe a un único correo en la base de datos
    private String email;

    private String celular;

    @OneToMany(mappedBy = "profesores", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Materia> materias;

    public Profesor() {
        this.materias = new ArrayList<>();
    }


    // Getters y Setters
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id; 
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public List<Materia> getMaterias(){
        return materias; 
    }

    public void setMaterias(List<Materia> materias){
        this.materias = materias;
    }
}
