package com.backend_libertadores.libertadoresback.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend_libertadores.libertadoresback.domain.Estudiante;
import com.backend_libertadores.libertadoresback.dto.EstudianteDTO;
import com.backend_libertadores.libertadoresback.infrastructure.EstudianteRepository;
import com.backend_libertadores.libertadoresback.services.EstudianteService;

@RestController
@RequestMapping("/api/estudiantes")
@CrossOrigin(origins = "http://localhost:5500")
public class EstudianteController {

    @Autowired
    private EstudianteRepository estudianteRepository;


    @Autowired
    private EstudianteService estudianteService;

    @GetMapping
    public List<Estudiante> listarEstudiantes() {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        for (Estudiante estudiante : estudiantes) {
            // Asegúrate de que la relación usuario se carga
            if (estudiante.getUsuario() != null) {
                estudiante.getUsuario();  // Forzamos la carga del usuario
            }
        }
        return estudiantes;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstudiante(@PathVariable Long id) {
        try {
            // Llama al servicio para manejar toda la lógica
            estudianteService.eliminarEstudianteYUsuario(id);
            return ResponseEntity.ok("Estudiante eliminado exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el estudiante: " + e.getMessage());
        }
    }

    @GetMapping(params = "email")
    public ResponseEntity<Object> buscarEstudiantePorEmail(@RequestParam String email) {
        return estudianteRepository.findByUsuarioEmail(email)
                .map(estudiante -> ResponseEntity.ok((Object) estudiante)) // Éxito: devuelve el estudiante
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body((Object) "Estudiante no encontrado")); // Error: devuelve mensaje
    }

      // Obtener detalles de un estudiante por su ID
    @GetMapping("/{estudianteId}")
    public ResponseEntity<Estudiante> obtenerEstudiantePorId(@PathVariable Long estudianteId) {
        return estudianteRepository.findById(estudianteId)
                .map(estudiante -> ResponseEntity.ok(estudiante))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/curso/{cursoId}/materia/{materiaId}")
    public ResponseEntity<List<EstudianteDTO>> obtenerEstudiantesPorCursoYMateria(
            @PathVariable Long cursoId,
            @PathVariable Long materiaId) {

        List<Estudiante> estudiantes = estudianteRepository.findByCursoIdAndMateriaId(cursoId, materiaId);

        // Convertir a DTO
        List<EstudianteDTO> estudiantesDTO = estudiantes.stream()
                .map(est -> new EstudianteDTO(est.getId(), est.getUsuario().getNombre(), est.getUsuario().getEmail()))
                .toList();

        return ResponseEntity.ok(estudiantesDTO);
    }

}