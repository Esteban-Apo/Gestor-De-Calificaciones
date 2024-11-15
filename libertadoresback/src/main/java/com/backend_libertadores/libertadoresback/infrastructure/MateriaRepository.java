package com.backend_libertadores.libertadoresback.infrastructure;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend_libertadores.libertadoresback.domain.Materia;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long> {
  
}
