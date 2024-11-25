package com.backend_libertadores.libertadoresback.services;

import java.util.List;
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
        // Obtener las calificaciones desde la base de datos
        List<Calificacion> calificaciones = calificacionRepository.findByMateriaIdAndEstudianteCursoId(materiaId, cursoId);

        // Convertir las entidades Calificacion a CalificacionDTO
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
            Calificacion calificacion = new Calificacion();

            // Mapear el DTO a la entidad Calificacion
            calificacion.setEstudiante(estudiantesRepository.findById(dto.getEstudianteId()).orElseThrow(() -> new RuntimeException("Estudiante no encontrado")));
            calificacion.setMateria(materiaRepository.findById(dto.getMateriaId()).orElseThrow(() -> new RuntimeException("Materia no encontrada")));
            calificacion.setPeriodo1(dto.getPeriodo1());
            calificacion.setPeriodo2(dto.getPeriodo2());
            calificacion.setPeriodo3(dto.getPeriodo3());

            return calificacion;
        }).collect(Collectors.toList());

        return calificacionRepository.saveAll(calificaciones);
    }
}