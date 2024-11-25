package com.backend_libertadores.libertadoresback.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend_libertadores.libertadoresback.domain.Curso;
import com.backend_libertadores.libertadoresback.domain.Estudiante;
import com.backend_libertadores.libertadoresback.infrastructure.CursoRepository;
import com.backend_libertadores.libertadoresback.infrastructure.EstudianteRepository;
import com.backend_libertadores.libertadoresback.services.CursoService;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "http://localhost:5500")
public class CursoController {

     @Autowired
    private CursoRepository cursoRepository;


    @Autowired
    private CursoService cursoService;

    @Autowired
    private EstudianteRepository estudianteRepository;



    @GetMapping
    public List<Curso> listarCursos() {
        return cursoService.obtenerCursos();
    }
    

    @PostMapping
    public Curso createCurso(@RequestBody Curso curso) {
        return cursoRepository.save(curso);
    }

    @DeleteMapping("/{id}")
    public void deleteCurso(@PathVariable Long id) {
        cursoRepository.deleteById(id);
    }

    /*Logica*/ 
    // Obtener estudiantes de un curso
    // Obtener estudiantes asociados al curso con un nuevo path
    @GetMapping("/{id}/estudiantes/detalle")
    public ResponseEntity<?> obtenerEstudiantes(@PathVariable Long id) {
        return cursoService.obtenerCursoPorId(id)
                .map(curso -> ResponseEntity.ok(curso.getEstudiantes()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{cursoId}/estudiantes")
    public ResponseEntity<List<Estudiante>> obtenerEstudiantesPorCurso(@PathVariable Long cursoId) {
        List<Estudiante> estudiantes = cursoService.obtenerEstudiantesPorCurso(cursoId);
        return ResponseEntity.ok(estudiantes);
    }

    // Agregar un estudiante a un curso
    @PostMapping("/{cursoId}/estudiantes/{estudianteId}")
    public ResponseEntity<?> agregarEstudianteACurso(
            @PathVariable Long cursoId, @PathVariable Long estudianteId) {
        try {
            // Verificar si el estudiante existe
            Estudiante estudiante = estudianteRepository.findById(estudianteId)
                    .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));

            // Verificar si el estudiante ya está asignado a un curso
            if (estudiante.getCurso() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El estudiante ya está asignado al curso: " + estudiante.getCurso().getNombreCurso());
            }

            // Verificar si el curso existe
            Curso curso = cursoRepository.findById(cursoId)
                    .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

            // Asignar el estudiante al curso
            estudiante.setCurso(curso);
            estudianteRepository.save(estudiante);

            return ResponseEntity.ok("Estudiante asignado al curso exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}