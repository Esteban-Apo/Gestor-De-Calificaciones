package com.backend_libertadores.libertadoresback.dto;

public class EstudianteDTO {
    private Long id;
    private String nombre;
    private String email;

    // Constructor
    public EstudianteDTO(Long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email; 
    }

    // Getters y setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email; 
    }
}
