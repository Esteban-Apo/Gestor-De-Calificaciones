package com.backend_libertadores.libertadoresback.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend_libertadores.libertadoresback.domain.Curso;


public interface CursoRepository extends JpaRepository<Curso, Long> {
    
}