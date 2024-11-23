package com.backend_libertadores.libertadoresback.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend_libertadores.libertadoresback.domain.Curso;
import com.backend_libertadores.libertadoresback.domain.Estudiante;
import com.backend_libertadores.libertadoresback.infrastructure.CursoRepository;
import com.backend_libertadores.libertadoresback.infrastructure.EstudianteRepository;

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

    // Agregar estudiante a un curso
    public Curso agregarEstudianteACurso(Long cursoId, Long estudianteId) {
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);

        if (cursoOpt.isPresent() && estudianteOpt.isPresent()) {
            Curso curso = cursoOpt.get();
            Estudiante estudiante = estudianteOpt.get();

            estudiante.setCurso(curso); // Relacionar estudiante con el curso
            estudianteRepository.save(estudiante);

            return curso;
        } else {
            throw new IllegalArgumentException("Curso o estudiante no encontrado.");
        }
    }
}