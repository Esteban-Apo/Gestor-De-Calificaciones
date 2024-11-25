package com.backend_libertadores.libertadoresback.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend_libertadores.libertadoresback.domain.Curso;
import com.backend_libertadores.libertadoresback.domain.Estudiante;
import com.backend_libertadores.libertadoresback.infrastructure.CursoRepository;
import com.backend_libertadores.libertadoresback.infrastructure.EstudianteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CursoService {

    @Autowired
    private final CursoRepository cursoRepository;

     @Autowired
    private final EstudianteRepository estudianteRepository;


    public CursoService(CursoRepository cursoRepository, EstudianteRepository estudianteRepository) {
        this.cursoRepository = cursoRepository;
        this.estudianteRepository = estudianteRepository; 
    }

    // Obtener todos los cursos
    public List<Curso> obtenerCursos() {
        return cursoRepository.findAll();
    }

      // Obtener un curso por ID
    public Optional<Curso> obtenerCursoPorId(Long id) {
        return cursoRepository.findById(id);
    }

    public List<Estudiante> obtenerEstudiantesPorCurso(Long cursoId) {
        Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        return curso.getEstudiantes(); // Devuelve la lista directamente
    }

    // Agregar estudiante a un curso
    public Curso agregarEstudianteACurso(Long cursoId, Long estudianteId) {
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));

        if (estudiante.getCurso() != null) {
            throw new IllegalArgumentException("El estudiante ya está asignado a un curso");
        }

        curso.getEstudiantes().add(estudiante);
        estudiante.setCurso(curso);

        return cursoRepository.save(curso);
    }
}