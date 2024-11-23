package com.backend_libertadores.libertadoresback.infrastructure;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend_libertadores.libertadoresback.domain.Estudiante;
import com.backend_libertadores.libertadoresback.domain.Usuario;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByUsuarioEmail(String email);
    Estudiante findByUsuario(Usuario usuario);
}
