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
class UsuarioControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private NotaRepository notaRepository;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Categoría 1");
    }

    @Test
    void testObtenerTodas() {
        System.out.println("Ejecutando prueba: testObtenerTodas");
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));
        ResponseEntity<List<Usuario>> responseEntity = usuarioController.obtenerTodas();
        
        List<Usuario> usuarios = responseEntity.getBody();
        System.out.println("Datos enviados a la API: []"); // No hay datos enviados en este caso
        System.out.println("Datos devueltos de la API: " + usuarios);

        Assertions.assertNotNull(usuarios);
        Assertions.assertEquals(1, usuarios.size());
        Assertions.assertEquals("Categoría 1", usuarios.get(0).getNombre());
    }

    @Test
    void testObtenerPorId() {
        System.out.println("Ejecutando prueba: testObtenerPorId");
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        ResponseEntity<Usuario> responseEntity = usuarioController.obtenerPorId(1L);
        
        System.out.println("Datos enviados a la API: ID = 1");
        System.out.println("Datos devueltos de la API: " + responseEntity.getBody());

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(usuario, responseEntity.getBody());
    }

    @Test
    void testCrearUsuario() {
        System.out.println("Ejecutando prueba: testCrearUsuario");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        
        System.out.println("Datos enviados a la API: " + usuario);
        ResponseEntity<Usuario> response = usuarioController.crearUsuario(usuario);
        System.out.println("Datos devueltos de la API: " + response.getBody());

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(usuario, response.getBody());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testActualizarUsuario() {
        System.out.println("Ejecutando prueba: testActualizarUsuario");
        when(usuarioRepository.existsById(anyLong())).thenReturn(true);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        
        System.out.println("Datos enviados a la API: ID = 1, " + usuario);
        ResponseEntity<Usuario> response = usuarioController.actualizarUsuario(1L, usuario);
        System.out.println("Datos devueltos de la API: " + response.getBody());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(usuario, response.getBody());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testActualizarUsuarioNoEncontrada() {
        System.out.println("Ejecutando prueba: testActualizarUsuarioNoEncontrada");
        when(usuarioRepository.existsById(anyLong())).thenReturn(false);
        ResponseEntity<Usuario> response = usuarioController.actualizarUsuario(1L, usuario);
        
        System.out.println("Datos enviados a la API: ID = 1, " + usuario);
        System.out.println("Datos devueltos de la API: " + response.getStatusCode());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testEliminarUsuario() {
        System.out.println("Ejecutando prueba: testEliminarUsuario");
        when(usuarioRepository.existsById(anyLong())).thenReturn(true);
        when(notaRepository.findByUsuarioId(anyLong())).thenReturn(Arrays.asList());
        doNothing().when(usuarioRepository).deleteById(anyLong());
        
        System.out.println("Datos enviados a la API: ID = 1");
        ResponseEntity<Void> response = usuarioController.eliminarUsuario(1L);
        System.out.println("Datos devueltos de la API: " + response.getStatusCode());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(usuarioRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testEliminarUsuarioConNotasAsociados() {
        System.out.println("Ejecutando prueba: testEliminarUsuarioConNotasAsociados");
        when(usuarioRepository.existsById(anyLong())).thenReturn(true);
        when(notaRepository.findByUsuarioId(anyLong())).thenReturn(Arrays.asList(new Nota()));
        
        System.out.println("Datos enviados a la API: ID = 1");
        ResponseEntity<Void> response = usuarioController.eliminarUsuario(1L);
        System.out.println("Datos devueltos de la API: " + response.getStatusCode());

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testObtenerNotasPorUsuario() {
        System.out.println("Ejecutando prueba: testObtenerNotasPorUsuario");
        when(usuarioRepository.existsById(anyLong())).thenReturn(true);
        when(notaRepository.findByUsuarioId(anyLong())).thenReturn(Arrays.asList(new Nota()));
        
        System.out.println("Datos enviados a la API: Usuario ID = 1");
        ResponseEntity<List<Nota>> response = usuarioController.obtenerNotasPorUsuario(1L);
        System.out.println("Datos devueltos de la API: " + response.getStatusCode());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
