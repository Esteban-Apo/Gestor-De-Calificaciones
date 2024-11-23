package com.backend_libertadores.libertadoresback.web;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @PostMapping("/agregar")
    public ResponseEntity<?> agregarProfesor(@RequestBody Profesor profesor) {
        // Verificar si el correo ya existe
        if (profesorRepository.findByEmail(profesor.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo electrónico ya está en uso");
        }
        Profesor nuevoProfesor = profesorRepository.save(profesor);
        return ResponseEntity.ok(nuevoProfesor);
    }

 
    @GetMapping
    public ResponseEntity<List<Profesor>> obtenerProfesores() {
        List<Profesor> profesores = profesorRepository.findAll();
        return ResponseEntity.ok(profesores);
    }

    //Eliminar a un profesor
    @DeleteMapping("/{profesorId}")
    public ResponseEntity<String> eliminarProfesor(@PathVariable Long profesorId) {
        // Buscar el profesor
        Profesor profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado."));

        // Desasociar al profesor de cualquier materia que tenga asignada
        List<Materia> materias = materiaRepository.findByProfesores(profesor);
        for (Materia materia : materias) {
            materia.setProfesores(null);
            materiaRepository.save(materia);
        }

        // Eliminar al profesor
        profesorRepository.delete(profesor);

        return ResponseEntity.ok("Profesor eliminado exitosamente.");
    }

     
    @PostMapping("{profesorId}/materias")
    public ResponseEntity<String> asignarMateriaAProfesor( @PathVariable Long profesorId, @RequestBody Map<String, Long> payload) {

        Long materiaId = payload.get("materiaId");
        Long cursoId = payload.get("cursoId");

        // Validar que los datos sean correctos
        if (materiaId == null || cursoId == null) {
            return ResponseEntity.badRequest().body("Debe proporcionar los IDs de materia y curso.");
        }

        // Buscar el profesor
        Profesor profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado."));

        // Buscar la materia
        Materia materia = materiaRepository.findById(materiaId)
                .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada."));

        // Validar que la materia pertenece al curso seleccionado
        if (!materia.getCurso().getId().equals(cursoId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La materia seleccionada no pertenece al curso proporcionado.");
        }

        // Verificar si la materia ya tiene un profesor asignado
        if (materia.getProfesores() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta materia ya tiene un profesor asignado.");
        }

        // Asignar el profesor a la materia
        materia.setProfesores(profesor);
        materiaRepository.save(materia);

        return ResponseEntity.ok("Materia asignada exitosamente.");
    }

   @GetMapping("/conMaterias")
    public List<Map<String, Object>> listarProfesoresConMaterias() throws JsonProcessingException {
        List<Profesor> profesores = profesorRepository.findAll();

        List<Map<String, Object>> respuesta = profesores.stream().map(profesor -> {
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("id", profesor.getId());
            resultado.put("nombreCompleto", profesor.getNombreCompleto());
            resultado.put("email", profesor.getEmail());
            resultado.put("celular", profesor.getCelular());

            List<Map<String, String>> materias = profesor.getMaterias().stream().map(materia -> {
                Map<String, String> materiaInfo = new HashMap<>();
                materiaInfo.put("nombre", materia.getNombre());
                if (materia.getCurso() != null) {
                    materiaInfo.put("curso", materia.getCurso().getNombreCurso());
                } else {
                    materiaInfo.put("curso", "Sin curso asignado");
                }
                return materiaInfo;
            }).toList();

            resultado.put("materias", materias);
            return resultado;
        }).toList();

        // Log para revisar el JSON generado
        System.out.println(new ObjectMapper().writeValueAsString(respuesta));
        return respuesta;
    }

}
