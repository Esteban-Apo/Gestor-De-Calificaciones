package com.backend_libertadores.libertadoresback.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend_libertadores.libertadoresback.domain.Calificacion;
import com.backend_libertadores.libertadoresback.dto.CalificacionDTO;
import com.backend_libertadores.libertadoresback.infrastructure.CalificacionRepository;
import com.backend_libertadores.libertadoresback.infrastructure.EstudianteRepository;
import com.backend_libertadores.libertadoresback.infrastructure.MateriaRepository;

@Service
public class CalificacionService {

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private EstudianteRepository estudiantesRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    public List<CalificacionDTO> obtenerCalificaciones(Long cursoId, Long materiaId) {
        List<Calificacion> calificaciones = calificacionRepository.findByMateriaIdAndEstudianteCursoId(materiaId, cursoId);

        return calificaciones.stream().map(calificacion -> {
            CalificacionDTO dto = new CalificacionDTO();
            dto.setEstudianteId(calificacion.getEstudiante().getId());
            dto.setMateriaId(calificacion.getMateria().getId());
            dto.setPeriodo1(calificacion.getPeriodo1());
            dto.setPeriodo2(calificacion.getPeriodo2());
            dto.setPeriodo3(calificacion.getPeriodo3());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<Calificacion> guardarCalificaciones(List<CalificacionDTO> calificacionDTOs) {
        List<Calificacion> calificaciones = calificacionDTOs.stream().map(dto -> {
            // Verificar si ya existe una calificación para el estudiante y la materia
            Optional<Calificacion> calificacionExistenteOpt = calificacionRepository.findByEstudianteIdAndMateriaId(dto.getEstudianteId(), dto.getMateriaId());

            Calificacion calificacion;
            if (calificacionExistenteOpt.isPresent()) {
                // Actualizar la calificación existente
                calificacion = calificacionExistenteOpt.get();
                calificacion.setPeriodo1(dto.getPeriodo1());
                calificacion.setPeriodo2(dto.getPeriodo2());
                calificacion.setPeriodo3(dto.getPeriodo3());
            } else {
                // Crear una nueva calificación
                calificacion = new Calificacion();
                calificacion.setEstudiante(estudiantesRepository.findById(dto.getEstudianteId())
                        .orElseThrow(() -> new RuntimeException("Estudiante no encontrado")));
                calificacion.setMateria(materiaRepository.findById(dto.getMateriaId())
                        .orElseThrow(() -> new RuntimeException("Materia no encontrada")));
                calificacion.setPeriodo1(dto.getPeriodo1());
                calificacion.setPeriodo2(dto.getPeriodo2());
                calificacion.setPeriodo3(dto.getPeriodo3());
            }

            return calificacion;
        }).collect(Collectors.toList());

        return calificacionRepository.saveAll(calificaciones);
    }
    
   
    public CalificacionDTO obtenerCalificacionPorEstudianteYMateria(Long estudianteId, Long materiaId) {
        Optional<Calificacion> calificacionOpt = calificacionRepository.findByEstudianteIdAndMateriaId(estudianteId, materiaId);

        if (calificacionOpt.isPresent()) {
            Calificacion calificacion = calificacionOpt.get();
            return new CalificacionDTO(
                    calificacion.getEstudiante().getId(),
                    calificacion.getMateria().getId(),
                    calificacion.getPeriodo1(),
                    calificacion.getPeriodo2(),
                    calificacion.getPeriodo3()
            );
        }

        return null;
    }

}