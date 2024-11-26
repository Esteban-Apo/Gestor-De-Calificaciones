package com.backend_libertadores.libertadoresback.services;



import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend_libertadores.libertadoresback.domain.Estudiante;
import com.backend_libertadores.libertadoresback.domain.Usuario;
import com.backend_libertadores.libertadoresback.infrastructure.EstudianteRepository;
import com.backend_libertadores.libertadoresback.infrastructure.UsuarioRepository;



@Service // Anotación clase de tipo servicio
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository; 

    @Autowired
    private EstudianteRepository estudianteRepository;

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

    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }
        usuarioRepository.deleteById(id);
    }
    

    //Métodos de autenticación y registro

    public Optional<Usuario> autenticarUsuario(String email, String contraseña) { // Autenticar usuario al iniciar sesión
        return usuarioRepository.findByEmailAndContraseña(email, contraseña);
    }

    //Registrar Usuario
   public Usuario registrarUsuario(Usuario usuario) {
    if (emailExiste(usuario.getEmail())) {
        throw new IllegalArgumentException("El correo ya está registrado.");
    }

    if (!validarPassword(usuario.getContraseña())) {
        throw new IllegalArgumentException("La contraseña debe de tener almenos 8 caracteres, una mayuscula, una minuscula, un numero y un caracter especial.");
    }

    usuario.setRol("Estudiante");
    Usuario nuevoUsuario = usuarioRepository.save(usuario);

    // Crear el registro de estudiante
    Estudiante estudiante = new Estudiante(nuevoUsuario);
    estudianteRepository.save(estudiante);

    return nuevoUsuario;
}

    

    // *** Métodos de validación ***

    // Validar si el correo ya está registrado
    public boolean emailExiste(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    // Método para buscar un usuario por email
      public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Validar si la contraseña cumple con los requisitos de seguridad
    public boolean validarPassword(String password) {
        return pattern.matcher(password).matches();
    }

    public List<Usuario> findByRol(String rol) {
        return usuarioRepository.findByRol(rol); // Encontrar por Rol
    }

   
}
