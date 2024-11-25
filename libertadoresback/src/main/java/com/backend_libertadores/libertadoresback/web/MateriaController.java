package com.backend_libertadores.libertadoresback.web;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.backend_libertadores.libertadoresback.infrastructure.ProfesorRepository;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    @Autowired
    private final MateriaRepository materiaRepository;
    private final CursoRepository cursoRepository;

   

    public MateriaController(MateriaRepository materiaRepository, CursoRepository cursoRepository, ProfesorRepository profesorRepository) {
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
    

    @PostMapping("/curso/{cursoId}")
    public ResponseEntity<?> agregarMateriaACurso(@PathVariable Long cursoId, @RequestBody Map<String, Object> payload) {
        try {
            // Recupera el nombre de la materia
            String nombreMateria = (String) payload.get("nombre");
    
            // Buscar el curso al que se asignará la materia
            Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));
    
            // Verificar si ya existe una materia con el mismo nombre en el curso
            Optional<Materia> materiaExistente = materiaRepository.findByNombreAndCurso(nombreMateria, curso);
            if (materiaExistente.isPresent() && materiaExistente.get().getProfesores() != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Esta materia ya existe en este curso."));
            }
    
            // Crear la nueva materia
            Materia nuevaMateria = new Materia();
            nuevaMateria.setNombre(nombreMateria);
            nuevaMateria.setCurso(curso);
    
            // No asignamos profesor inicialmente, por lo que será null
            nuevaMateria.setProfesores(null);
    
            // Guardar la materia en el repositorio
            materiaRepository.save(nuevaMateria);
    
            return ResponseEntity.ok(Collections.singletonMap("message", "Materia creada exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear la materia: " + e.getMessage());
        }
    }


    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Materia>> obtenerMateriasPorCurso(@PathVariable Long cursoId) {
        List<Materia> materias = materiaRepository.findByCursoId(cursoId);
        return ResponseEntity.ok(materias);
    }

}
