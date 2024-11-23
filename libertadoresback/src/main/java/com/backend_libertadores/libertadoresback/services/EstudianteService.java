package com.backend_libertadores.libertadoresback.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend_libertadores.libertadoresback.domain.Estudiante;
import com.backend_libertadores.libertadoresback.infrastructure.EstudianteRepository;
import com.backend_libertadores.libertadoresback.infrastructure.UsuarioRepository;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void eliminarEstudianteYUsuario(Long estudianteId) {
        // Verificar si el estudiante existe
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);

        if (estudianteOpt.isPresent()) {
            Estudiante estudiante = estudianteOpt.get();

            // Eliminar el usuario asociado
            Long usuarioId = estudiante.getUsuario().getId();
            usuarioRepository.deleteById(usuarioId);

            // Eliminar el estudiante
            estudianteRepository.deleteById(estudianteId);
        } else {
            throw new IllegalArgumentException("Estudiante no encontrado.");
        }
    }
}