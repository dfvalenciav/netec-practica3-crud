package com.company.spring_boot_crud_app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;
    private final NotaRepository notaRepository;

    public UsuarioController(UsuarioRepository usuarioRepository, NotaRepository notaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.notaRepository = notaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodas() {
        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario nuevaUsuario = usuarioRepository.save(usuario);
        return new ResponseEntity<>(nuevaUsuario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuario.setId(id);
        Usuario actualizada = usuarioRepository.save(usuario);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Verificar si hay notas asociados a esta categoría
        List<Nota> notasAsociadas = notaRepository.findByUsuarioId(id);
        if (!notasAsociadas.isEmpty()) {
            return ResponseEntity.badRequest().build(); // No se puede eliminar una categoría con notas asociados
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/notas")
    public ResponseEntity<List<Nota>> obtenerNotasPorUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<Nota> notas = notaRepository.findByUsuarioId(id);
        return ResponseEntity.ok(notas);
    }
}
