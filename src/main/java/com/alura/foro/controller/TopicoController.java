package com.alura.foro.controller;

import com.alura.foro.model.Topico;
import com.alura.foro.repository.TopicoRepository;
import com.alura.foro.dto.DatosRegistroTopico;
import com.alura.foro.dto.DatosListadoTopico;
import com.alura.foro.dto.DatosActualizarTopico;

import org.springframework.transaction.annotation.Transactional;;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.net.URI;

// imports para paginación
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    // GET /topicos → lista paginada de tópicos
    @GetMapping
    public Page<DatosListadoTopico> listar(@PageableDefault(size = 10) Pageable pageable) {
        return repository.findAll(pageable)
                .map(topico -> new DatosListadoTopico(
                        topico.getId(),
                        topico.getTitulo(),
                        topico.getMensaje(),
                        topico.getFechaCreacion(),
                        topico.getStatus() != null ? topico.getStatus().toString() : null,
                        topico.getAutor(),
                        topico.getCurso()
                ));
    }

    // POST /topicos → registra un nuevo tópico y devuelve 201 Created
    @PostMapping
    public ResponseEntity<DatosListadoTopico> registrar(@RequestBody @Valid DatosRegistroTopico datos) {
        if (repository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            return ResponseEntity
                    .badRequest()
                    .body(null); // mantengo tu comportamiento
        }

        Topico topico = new Topico(datos);
        repository.save(topico);

        DatosListadoTopico body = new DatosListadoTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus() != null ? topico.getStatus().toString() : null,
                topico.getAutor(),
                topico.getCurso()
        );

        return ResponseEntity
                .created(URI.create("/topicos/" + topico.getId()))
                .body(body);
    }

    // GET /topicos/{id} → detalle con el mismo DTO de 7 campos
    @GetMapping("/{id}")
    public ResponseEntity<DatosListadoTopico> detalle(@PathVariable Long id) {
        return repository.findById(id)
                .map(t -> ResponseEntity.ok(
                        new DatosListadoTopico(
                                t.getId(),
                                t.getTitulo(),
                                t.getMensaje(),
                                t.getFechaCreacion(),
                                t.getStatus() != null ? t.getStatus().toString() : null,
                                t.getAutor(),
                                t.getCurso()
                        )
                ))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /topicos/{id} → actualiza un tópico existente (devuelve el mismo DTO)
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosListadoTopico> actualizar(@PathVariable Long id, @RequestBody @Valid DatosActualizarTopico datos) {
        var topicoOptional = repository.findById(id);

        if (topicoOptional.isPresent()) {
            Topico topico = topicoOptional.get();

            if (datos.titulo() != null) topico.setTitulo(datos.titulo());
            if (datos.mensaje() != null) topico.setMensaje(datos.mensaje());
            // if (datos.status() != null) topico.setStatus(datos.status());

            repository.save(topico);

            DatosListadoTopico body = new DatosListadoTopico(
                    topico.getId(),
                    topico.getTitulo(),
                    topico.getMensaje(),
                    topico.getFechaCreacion(),
                    topico.getStatus() != null ? topico.getStatus().toString() : null,
                    topico.getAutor(),
                    topico.getCurso()
            );

            return ResponseEntity.ok(body);
        }

        return ResponseEntity.notFound().build();
    }

    // DELETE /topicos/{id} → elimina un tópico por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.notFound().build(); // 404
    }
}
