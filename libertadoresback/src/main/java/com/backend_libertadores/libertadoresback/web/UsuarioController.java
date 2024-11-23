package com.backend_libertadores.libertadoresback.web;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend_libertadores.libertadoresback.domain.Usuario;
import com.backend_libertadores.libertadoresback.services.UsuarioService;



@RestController
@RequestMapping("/api/usuarios") 
@CrossOrigin(origins = "http://localhost:5500")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService; 

    @PostMapping
    public Usuario crearUsuario (@RequestBody Usuario usuario) {
       return usuarioService.crearUsuario(usuario);
    }

    @GetMapping("/detalle/{id}")
    public Optional<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.obtenerUsuarioPorId(id);
    }

    @GetMapping
    public List <Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuario(id); // Eliminar usuario
            return ResponseEntity.ok("Usuario eliminado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar el usuario.");
        }
    }
    

    
    @GetMapping("/profesores")
    public List<Usuario> getProfesores() {
        return usuarioService.findByRol("Profesor");
    }

    @GetMapping("/estudiantes")
    public List<Usuario> getEstudiantes() {
        return usuarioService.findByRol("Estudiante");
    }

    //metodo para logear un usuario 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
    Optional<Usuario> usuarioAutenticado = usuarioService.autenticarUsuario(usuario.getEmail(), usuario.getContraseña());

        if (usuarioAutenticado.isPresent()) {
            return ResponseEntity.ok(usuarioAutenticado.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }

     // Método para registrar un nuevo usuario
     @PostMapping("/register")
     public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
         try {
             Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
             return ResponseEntity.ok(nuevoUsuario);
         } catch (IllegalArgumentException e) {
             // Devuelve un error 409 con un cuerpo JSON
             return ResponseEntity.status(HttpStatus.CONFLICT)
                                  .body(Map.of("error", e.getMessage()));
         }
     }
}