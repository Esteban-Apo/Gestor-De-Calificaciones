package com.backend_libertadores.libertadoresback.domain;


import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal periodo1;
    private BigDecimal periodo2;
    private BigDecimal periodo3;

    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "materia_id")
    private Materia materia;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPeriodo1() {
        return periodo1;
    }

    public void setPeriodo1(BigDecimal periodo1) {
        this.periodo1 = periodo1;
    }

    public BigDecimal getPeriodo2() {
        return periodo2;
    }

    public void setPeriodo2(BigDecimal periodo2) {
        this.periodo2 = periodo2;
    }

    public BigDecimal getPeriodo3() {
        return periodo3;
    }

    public void setPeriodo3(BigDecimal periodo3) {
        this.periodo3 = periodo3;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }
}