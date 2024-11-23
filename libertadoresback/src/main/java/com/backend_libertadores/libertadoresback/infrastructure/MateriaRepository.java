package com.backend_libertadores.libertadoresback.infrastructure;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend_libertadores.libertadoresback.domain.Curso;
import com.backend_libertadores.libertadoresback.domain.Materia;
import com.backend_libertadores.libertadoresback.domain.Profesor;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
    Optional<Materia> findByNombreAndCurso(String nombreMateria, Curso curso);
    List<Materia> findByCursoId(Long cursoId);
    List<Materia> findByProfesores(Profesor profesor);
}
