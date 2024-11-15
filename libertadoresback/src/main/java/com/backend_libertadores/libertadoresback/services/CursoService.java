package com.backend_libertadores.libertadoresback.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend_libertadores.libertadoresback.domain.Curso;
import com.backend_libertadores.libertadoresback.infrastructure.CursoRepository;

@Service
public class CursoService {

    @Autowired
    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> obtenerCursos() {
        return cursoRepository.findAll();
    }
}