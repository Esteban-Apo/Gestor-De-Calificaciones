package com.backend_libertadores.libertadoresback.web;

import java.util.List;
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

import com.backend_libertadores.libertadoresback.domain.Materia;
import com.backend_libertadores.libertadoresback.domain.Profesor;
import com.backend_libertadores.libertadoresback.infrastructure.MateriaRepository;
import com.backend_libertadores.libertadoresback.infrastructure.ProfesorRepository;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {
    @Autowired
    private final ProfesorRepository profesorRepository;

    @Autowired
    private final MateriaRepository materiaRepository;

    public ProfesorController(ProfesorRepository profesorRepository, MateriaRepository materiaRepository) {
        this.profesorRepository = profesorRepository;
        this.materiaRepository = materiaRepository;
    }

    @PostMapping
    public ResponseEntity<?> agregarProfesor(@RequestBody Profesor profesor) {
        // Verificar si el correo ya existe
        if (profesorRepository.findByEmail(profesor.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("El correo electrónico ya está en uso");
        }
        Profesor nuevoProfesor = profesorRepository.save(profesor);
        return ResponseEntity.ok(nuevoProfesor);
    }

 
    @GetMapping
    public ResponseEntity<List<Profesor>> obtenerProfesores() {
        List<Profesor> profesores = profesorRepository.findAll();
        return ResponseEntity.ok(profesores);
    }

     //Eliminar profesores
    @DeleteMapping("/{id}")
    public void deleteProfesor(@PathVariable Long id) {
        profesorRepository.deleteById(id);
    }

     
    @PostMapping("{profesorId}/materias")
    public ResponseEntity<String> asignarMateriaAProfesor(@PathVariable Long profesorId, @RequestBody Long materiaId) {
        Optional<Profesor> profesorOptional = profesorRepository.findById(profesorId);
        Optional<Materia> materiaOptional = materiaRepository.findById(materiaId);
    
        if (profesorOptional.isPresent() && materiaOptional.isPresent()) {
            Profesor profesor = profesorOptional.get();
            Materia materia = materiaOptional.get();
            profesor.getMaterias().add(materia);
            profesorRepository.save(profesor);
            return ResponseEntity.ok("Materia asignada exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profesor o materia no encontrado");
        }
    }
    
}
