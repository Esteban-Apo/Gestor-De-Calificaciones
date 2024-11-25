package com.backend_libertadores.libertadoresback.infrastructure;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend_libertadores.libertadoresback.domain.Estudiante;
import com.backend_libertadores.libertadoresback.domain.Usuario;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByUsuarioEmail(String email);
    Estudiante findByUsuario(Usuario usuario);

    @Query("SELECT e FROM Estudiante e " +
        "JOIN e.curso c " +
        "JOIN c.materias m " +
        "WHERE c.id = :cursoId AND m.id = :materiaId")
    List<Estudiante> findByCursoIdAndMateriaId(@Param("cursoId") Long cursoId, @Param("materiaId") Long materiaId);

}
