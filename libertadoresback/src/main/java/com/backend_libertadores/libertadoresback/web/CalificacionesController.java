package com.backend_libertadores.libertadoresback.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend_libertadores.libertadoresback.dto.CalificacionDTO;
import com.backend_libertadores.libertadoresback.services.CalificacionService;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionesController {

    @Autowired
    private CalificacionService calificacionService;

    @GetMapping
    public ResponseEntity<List<CalificacionDTO>> obtenerCalificaciones(@RequestParam("cursoId") Long cursoId, @RequestParam("materiaId") Long materiaId) {
        List<CalificacionDTO> calificaciones = calificacionService.obtenerCalificaciones(cursoId, materiaId);
        return ResponseEntity.ok(calificaciones);
    }

    @PostMapping
    public ResponseEntity<Void> guardarCalificaciones(@RequestBody List<CalificacionDTO> calificacionesDTO) {
        calificacionService.guardarCalificaciones(calificacionesDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/estudiante/{estudianteId}/materia/{materiaId}")
    public ResponseEntity<CalificacionDTO> obtenerCalificacionPorEstudianteYMateria(
            @PathVariable Long estudianteId,
            @PathVariable Long materiaId) {
        CalificacionDTO calificacion = calificacionService.obtenerCalificacionPorEstudianteYMateria(estudianteId, materiaId);

        if (calificacion != null) {
            return ResponseEntity.ok(calificacion);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

