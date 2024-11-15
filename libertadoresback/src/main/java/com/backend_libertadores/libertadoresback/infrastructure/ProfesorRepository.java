package com.backend_libertadores.libertadoresback.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend_libertadores.libertadoresback.domain.Profesor;


public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
    Optional<Profesor> findByEmail(String email);
}
