package com.backend_libertadores.libertadoresback.dto;

import java.math.BigDecimal;

public class CalificacionDTO {

    private Long estudianteId;
    private BigDecimal periodo1;
    private BigDecimal periodo2;
    private BigDecimal periodo3;
    private Long materiaId;

    // Getters and Setters
    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
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

    public Long getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(Long materiaId) {
        this.materiaId = materiaId;
    }
}