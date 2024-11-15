package com.backend_libertadores.libertadoresback.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository; //peraciones comunes en la base de datos, guardar, buscar, eliminar, etc.

import com.backend_libertadores.libertadoresback.domain.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailAndContraseña(String email, String contraseña); //encontrar correo y contraseña especificos en base de datos
    Optional<Usuario> findByEmail(String email); //Buscar usuarios por correo (verificar correos duplicados)
    List<Usuario> findByRol(String rol);
}