package com.company.spring_boot_crud_app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotaControllerTest {

    @Mock
    private NotaRepository notaRepository;

    @Mock
    private UsuarioRepository categoriaRepository;

    @InjectMocks
    private NotaController notaController;

    private Nota nota;

    @BeforeEach
    public void setUp() {
        nota = new Nota();
        nota.setId(1L);
        nota.setNombre("Nota 1");
        nota.setResultado(100.0);
    }

    @Test
    void testObtenerTodos() {
        System.out.println("Ejecutando prueba: testObtenerTodos");
        when(notaRepository.findAll()).thenReturn(Arrays.asList(nota));
        ResponseEntity<List<Nota>> responseEntity = notaController.obtenerTodos();
        List<Nota> notas = responseEntity.getBody();
        
        System.out.println("Datos enviados a la API: []"); // No hay datos enviados en este caso
        System.out.println("Datos devueltos de la API: " + notas);

        Assertions.assertNotNull(notas);
        Assertions.assertEquals(1, notas.size());
        Assertions.assertEquals("Nota 1", notas.get(0).getNombre());
    }

    @Test
    void testObtenerPorId() {
        System.out.println("Ejecutando prueba: testObtenerPorId");
        when(notaRepository.findById(anyLong())).thenReturn(Optional.of(nota));
        ResponseEntity<Nota> responseEntity = notaController.obtenerPorId(1L);
        
        System.out.println("Datos enviados a la API: ID = 1");
        System.out.println("Datos devueltos de la API: " + responseEntity.getBody());

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(nota, responseEntity.getBody());
    }

    @Test
    void testCrearNota() {
        System.out.println("Ejecutando prueba: testCrearNota");
        when(notaRepository.save(any(Nota.class))).thenReturn(nota);
        
        System.out.println("Datos enviados a la API: " + nota);
        ResponseEntity<Nota> response = notaController.crearNota(nota);
        System.out.println("Datos devueltos de la API: " + response.getBody());

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(nota, response.getBody());
        verify(notaRepository, times(1)).save(any(Nota.class));
    }

    @Test
    void testActualizarNota() {
        System.out.println("Ejecutando prueba: testActualizarNota");
        when(notaRepository.existsById(anyLong())).thenReturn(true);
        when(notaRepository.save(any(Nota.class))).thenReturn(nota);
        
        System.out.println("Datos enviados a la API: ID = 1, " + nota);
        ResponseEntity<Nota> response = notaController.actualizarNota(1L, nota);
        System.out.println("Datos devueltos de la API: " + response.getBody());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(nota, response.getBody());
        verify(notaRepository, times(1)).save(any(Nota.class));
    }

    @Test
    void testActualizarNotaNoEncontrado() {
        System.out.println("Ejecutando prueba: testActualizarNotaNoEncontrado");
        when(notaRepository.existsById(anyLong())).thenReturn(false);
        ResponseEntity<Nota> response = notaController.actualizarNota(1L, nota);
        
        System.out.println("Datos enviados a la API: ID = 1, " + nota);
        System.out.println("Datos devueltos de la API: " + response.getStatusCode());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testEliminarNota() {
        System.out.println("Ejecutando prueba: testEliminarNota");
        when(notaRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(notaRepository).deleteById(anyLong());
        ResponseEntity<Void> response = notaController.eliminarNota(1L);
        
        System.out.println("Datos enviados a la API: ID = 1");
        System.out.println("Datos devueltos de la API: " + response.getStatusCode());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(notaRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testEliminarNotaNoEncontrado() {
        System.out.println("Ejecutando prueba: testEliminarNotaNoEncontrado");
        when(notaRepository.existsById(anyLong())).thenReturn(false);
        ResponseEntity<Void> response = notaController.eliminarNota(1L);
        
        System.out.println("Datos enviados a la API: ID = 1");
        System.out.println("Datos devueltos de la API: " + response.getStatusCode());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testObtenerPorUsuario() {
        System.out.println("Ejecutando prueba: testObtenerPorUsuario");
        when(categoriaRepository.existsById(anyLong())).thenReturn(true);
        when(notaRepository.findByUsuarioId(anyLong())).thenReturn(Arrays.asList(nota));
        ResponseEntity<List<Nota>> response = notaController.obtenerPorUsuario(1L);
        
        List<Nota> notas = response.getBody();
        System.out.println("Datos enviados a la API: Usuario ID = 1");
        System.out.println("Datos devueltos de la API: " + notas);

        Assertions.assertNotNull(notas);
        Assertions.assertEquals(1, notas.size());
        Assertions.assertEquals("Nota 1", notas.get(0).getNombre());
    }
}
