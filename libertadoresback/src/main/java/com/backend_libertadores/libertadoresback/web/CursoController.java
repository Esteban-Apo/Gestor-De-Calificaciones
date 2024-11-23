package com.backend_libertadores.libertadoresback.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.backend_libertadores.libertadoresback.infrastructure.CursoRepository;
import com.backend_libertadores.libertadoresback.services.CursoService;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "http://localhost:5500")
public class CursoController {

     @Autowired
    private CursoRepository cursoRepository;


    @Autowired
    private CursoService cursoService;



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
    @GetMapping("/{id}/estudiantes")
    public ResponseEntity<?> obtenerEstudiantes(@PathVariable Long id) {
        return cursoService.obtenerCursoPorId(id)
                .map(curso -> ResponseEntity.ok(curso.getEstudiantes()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Agregar un estudiante a un curso
    @PostMapping("/{cursoId}/estudiantes/{estudianteId}")
    public ResponseEntity<?> agregarEstudianteACurso(
            @PathVariable Long cursoId, @PathVariable Long estudianteId) {
        try {
            Curso curso = cursoService.agregarEstudianteACurso(cursoId, estudianteId);
            return ResponseEntity.ok(curso);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}