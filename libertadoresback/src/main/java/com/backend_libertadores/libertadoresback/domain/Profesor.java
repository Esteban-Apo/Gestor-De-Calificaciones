package com.backend_libertadores.libertadoresback.domain;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Profesor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;

    @Column(unique = true) // Restringe a un único correo en la base de datos
    private String email;

    private String celular;

    @ManyToMany
    @JoinTable(
        name = "profesor_materia",
        joinColumns = @JoinColumn(name = "profesor_id"),
        inverseJoinColumns = @JoinColumn(name = "materia_id")
    )
    @JsonIgnore // Ignora esta propiedad para evitar bucles
    private Set<Materia> materias;


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

    public Set<Materia> getMaterias(){
        return materias; 
    }

    public void setMaterias(Set<Materia> materias){
        this.materias = materias;
    }
}
