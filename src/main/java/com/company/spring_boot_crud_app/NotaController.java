package com.company.spring_boot_crud_app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notas")
public class NotaController {
    private final NotaRepository notaRepository;
    private final UsuarioRepository usuarioRepository;

    public NotaController(NotaRepository notaRepository, UsuarioRepository usuarioRepository) {
        this.notaRepository = notaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Nota>> obtenerTodos() {
        List<Nota> notas = (List<Nota>) notaRepository.findAll();
        return ResponseEntity.ok(notas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nota> obtenerPorId(@PathVariable Long id) {
        return notaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Nota> crearNota(@RequestBody Nota nota) {
        if (nota.getUsuario() != null && nota.getUsuario().getId() != null) {
            Optional<Usuario> usuario = usuarioRepository.findById(nota.getUsuario().getId());
            if (usuario.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            nota.setUsuario(usuario.get());
        }
        Nota nuevoNota = notaRepository.save(nota);
        return new ResponseEntity<>(nuevoNota, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nota> actualizarNota(@PathVariable Long id, @RequestBody Nota nota) {
        if (!notaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        nota.setId(id);
        if (nota.getUsuario() != null && nota.getUsuario().getId() != null) {
            Optional<Usuario> usuario = usuarioRepository.findById(nota.getUsuario().getId());
            if (usuario.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            nota.setUsuario(usuario.get());
        }
        Nota actualizado = notaRepository.save(nota);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNota(@PathVariable Long id) {
        if (!notaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        notaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Nota>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            return ResponseEntity.notFound().build();
        }
        List<Nota> notas = notaRepository.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(notas);
    }
}
