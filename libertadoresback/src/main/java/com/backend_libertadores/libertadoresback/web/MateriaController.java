package com.backend_libertadores.libertadoresback.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend_libertadores.libertadoresback.domain.Curso;
import com.backend_libertadores.libertadoresback.domain.Materia;
import com.backend_libertadores.libertadoresback.infrastructure.CursoRepository;
import com.backend_libertadores.libertadoresback.infrastructure.MateriaRepository;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    @Autowired
    private final MateriaRepository materiaRepository;
    private final CursoRepository cursoRepository;

    public MateriaController(MateriaRepository materiaRepository, CursoRepository cursoRepository) {
        this.materiaRepository = materiaRepository;
        this.cursoRepository = cursoRepository;
    }

    //CRUD
    @PostMapping
    public Materia createMateria(@RequestBody Materia materia) {
        return materiaRepository.save(materia);
    }

    @DeleteMapping("/{id}")
    public void deleteMateria(@PathVariable Long id) {
        materiaRepository.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<List<Materia>> obtenerMaterias() {
        List<Materia> materias = materiaRepository.findAll();
        return ResponseEntity.ok(materias);
    }
    

    //Agregar las materias a los cursos
    @PostMapping("/curso/{cursoId}")
    public ResponseEntity<?> agregarMateriaACurso(@PathVariable Long cursoId, @RequestBody Materia materia) {
    Optional<Curso> cursoOptional = cursoRepository.findById(cursoId);
    if (!cursoOptional.isPresent()) {
        return ResponseEntity.notFound().build();
    }
    
    Curso curso = cursoOptional.get();
    materia.setCurso(curso);
    Materia nuevaMateria = materiaRepository.save(materia);
    
    return ResponseEntity.ok(nuevaMateria);  // Verifica que aquí se devuelve solo el objeto "nuevaMateria"
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<?> obtenerMateriasPorCurso(@PathVariable Long cursoId) {
        Optional<Curso> cursoOptional = cursoRepository.findById(cursoId);
        if (!cursoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Curso curso = cursoOptional.get();
        return ResponseEntity.ok(curso.getMaterias());
    }

}
