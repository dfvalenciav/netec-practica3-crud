package com.company.spring_boot_crud_app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carpetas")
public class CarpetaController {

    private final CarpetaRepository carpetaRepository;
    private final NotaRepository notaRepository;

    public CarpetaController(CarpetaRepository carpetaRepository, NotaRepository notaRepository) {
        this.carpetaRepository = carpetaRepository;
        this.notaRepository = notaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Carpeta>> obtenerTodas() {
        List<Carpeta> carpetas = (List<Carpeta>) carpetaRepository.findAll();
        return ResponseEntity.ok(carpetas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carpeta> obtenerPorId(@PathVariable Long id) {
        return carpetaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Carpeta> crearCarpeta(@RequestBody Carpeta carpeta) {
        if (carpeta.getNombre() == null || carpeta.getNombre().isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Add validation check
        }
        Carpeta nuevaCarpeta = carpetaRepository.save(carpeta);
        return new ResponseEntity<>(nuevaCarpeta, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carpeta> actualizarCarpeta(@PathVariable Long id, @RequestBody Carpeta carpeta) {
        if (!carpetaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        carpeta.setId(id);
        Carpeta actualizada = carpetaRepository.save(carpeta);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCarpeta(@PathVariable Long id) {
        if (!carpetaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Verificar si hay notas asociados a esta categoría
        List<Nota> notasAsociados = notaRepository.findByCarpetaId(id);
        if (!notasAsociados.isEmpty()) {
            return ResponseEntity.badRequest().build(); // No se puede eliminar una categoría con notas asociados
        }
        carpetaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/notas")
    public ResponseEntity<List<Nota>> obtenerNotasPorCarpeta(@PathVariable Long id) {
        if (!carpetaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<Nota> notas = notaRepository.findByCarpetaId(id);
        return ResponseEntity.ok(notas);
    }
}