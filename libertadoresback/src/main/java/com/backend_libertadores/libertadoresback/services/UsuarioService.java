package com.backend_libertadores.libertadoresback.services;



import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend_libertadores.libertadoresback.domain.Usuario;
import com.backend_libertadores.libertadoresback.infrastructure.UsuarioRepository;



@Service // Anotación clase de tipo servicio
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository; 

    private static final String PASSWORD_PATTERN = 
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"; // Validación de una contraseña segura
    private final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);


    // *** Métodos CRUD***
    
    public List<Usuario> listarUsuarios() { // Retornar lista de todos los usuarios de la BD
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) { // Obtener un usuario por su ID
        return usuarioRepository.findById(id);
    }

    public Usuario crearUsuario(Usuario usuario) { // Crear un usuario nuevo
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) { // Eliminar usuario por su ID
        usuarioRepository.deleteById(id);
    }

    //Métodos de autenticación y registro

    public Optional<Usuario> autenticarUsuario(String email, String contraseña) { // Autenticar usuario al iniciar sesión
        return usuarioRepository.findByEmailAndContraseña(email, contraseña);
    }

    public Usuario registrarUsuario(Usuario usuario) { // Registrar un nuevo usuario
        if (emailExiste(usuario.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        if (!validarPassword(usuario.getContraseña())) {
            throw new IllegalArgumentException("La contraseña no cumple con los requisitos de seguridad.");
        }

        usuario.setRol("Estudiante");
        return usuarioRepository.save(usuario);
    }

    // *** Métodos de validación ***

    // Validar si el correo ya está registrado
    public boolean emailExiste(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    // Validar si la contraseña cumple con los requisitos de seguridad
    public boolean validarPassword(String password) {
        return pattern.matcher(password).matches();
    }

    public List<Usuario> findByRol(String rol) {
        return usuarioRepository.findByRol(rol); // Encontrar por Rol
    }

   
}
