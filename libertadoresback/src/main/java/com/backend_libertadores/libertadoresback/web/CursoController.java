package com.backend_libertadores.libertadoresback.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend_libertadores.libertadoresback.domain.Curso;
import com.backend_libertadores.libertadoresback.infrastructure.CursoRepository;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "http://localhost:5500")
public class CursoController {

     @Autowired
    private CursoRepository cursoRepository;



    @GetMapping
    public List<Curso> getCursos() {
        return cursoRepository.findAll();
    }

    @PostMapping
    public Curso createCurso(@RequestBody Curso curso) {
        return cursoRepository.save(curso);
    }

    @DeleteMapping("/{id}")
    public void deleteCurso(@PathVariable Long id) {
        cursoRepository.deleteById(id);
    }
}