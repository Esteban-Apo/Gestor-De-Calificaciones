package com.backend_libertadores.libertadoresback.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend_libertadores.libertadoresback.domain.Calificacion;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    List<Calificacion> findByMateriaIdAndEstudianteCursoId(Long materiaId, Long cursoId);
}
