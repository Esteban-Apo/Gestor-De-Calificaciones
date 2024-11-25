package com.backend_libertadores.libertadoresback.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonManagedReference;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;



@Entity
public class Materia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    
    @ManyToOne(fetch = FetchType.EAGER) // Asegúrate de usar carga EAGER para el curso
    @JoinColumn(name = "curso_id")
    @JsonIgnore // Evita que Jackson serialice el curso al convertir a JSON
    private Curso curso;
    

    @ManyToOne 
    @JoinColumn(name = "profesor_id", nullable = true)
    @JsonManagedReference
    private Profesor profesores;


    

    // Getters y Setters
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

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Profesor getProfesores() {
        return profesores; 
    }

    public void setProfesores(Profesor profesores){
        this.profesores = profesores; 
    }

    
}
