package com.company.spring_boot_crud_app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notas")
public class NotaWebController {
    
    private static final String REDIRECT_NOTAS = "redirect:/notas";
    private final NotaRepository notaRepository;
    private final CarpetaRepository carpetaRepository; // Add this line

    // Injecting both notaRepository and carpetaRepository
    public NotaWebController(NotaRepository notaRepository, CarpetaRepository carpetaRepository) {
        this.notaRepository = notaRepository;
        this.carpetaRepository = carpetaRepository; // Initialize carpetaRepository
    }

    @GetMapping
    public String listarNotas(Model model) {
        model.addAttribute("notas", notaRepository.findAll());
        return "notas/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("nota", new Nota());
        model.addAttribute("carpetas", carpetaRepository.findAll()); // Provide carpetas to the model
        return "notas/formulario";
    }

    @PostMapping("/nuevo")
    public String crearNota(@ModelAttribute Nota nota) {
        notaRepository.save(nota);
        return REDIRECT_NOTAS;
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de nota inválido: " + id));
        model.addAttribute("nota", nota);
        model.addAttribute("carpetas", carpetaRepository.findAll()); // Provide carpetas for editing
        return "notas/formulario";
    }

    @PostMapping("/editar/{id}")
    public String actualizarNota(@PathVariable Long id, @ModelAttribute Nota nota) {
        nota.setId(id);
        notaRepository.save(nota);
        return REDIRECT_NOTAS;
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarNota(@PathVariable Long id) {
        notaRepository.deleteById(id);
        return REDIRECT_NOTAS;
    }
}
