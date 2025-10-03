package com.bank.management.service;

import com.bank.management.dto.MessageResponseDTO;
import com.bank.management.dto.UsuarioRequestDTO;
import com.bank.management.dto.UsuarioResponseDTO;
import com.bank.management.entity.Usuario;
import com.bank.management.exception.DuplicatedDataException;
import com.bank.management.exception.UsuarioNotFoundException;
import com.bank.management.mapper.UsuarioMapper;
import com.bank.management.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
    private UsuarioRequestDTO requestDTO;
    private UsuarioResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Datos de prueba
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setEmail("juan@example.com");
        usuario.setPassword("password123");

        requestDTO = new UsuarioRequestDTO();
        requestDTO.setNombre("Juan");
        requestDTO.setEmail("juan@example.com");
        requestDTO.setPassword("password123");

        responseDTO = new UsuarioResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNombre("Juan");
        responseDTO.setEmail("juan@example.com");
    }

    // Guardar usuario Caso exitoso
    @Test
    void testGuardarUsuario_Exitoso() {
        //1.Preparea Datos
        when(usuarioRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(usuarioMapper.toEntity(requestDTO)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toResponseDto(usuario)).thenReturn(responseDTO);

        //2. Ejecutar
        UsuarioResponseDTO resultado = usuarioService.guardar(requestDTO);

        //3.Verificar
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());

        //5.Confirma
        verify(usuarioRepository, times(1)).save(usuario);
    }

    // Guardar usuario duplicado
    @Test
    void testGuardarUsuario_Duplicado() {
        //1. Prepara Datos
        when(usuarioRepository.existsByEmail(requestDTO.getEmail())).thenReturn(true);

        //2. Ejecuta y 3.verifica
        assertThrows(DuplicatedDataException.class, () -> usuarioService.guardar(requestDTO));

        //Confirma
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // Obtener todos los usuarios
    @Test
    void testObtenerTodos() {
        //1.Prepara los datos
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));
        when(usuarioMapper.toResponseDto(usuario)).thenReturn(responseDTO);

        //2. Ejecuta
        List<UsuarioResponseDTO> resultado = usuarioService.obtenerTodos();

        //3. Verifics
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());

        //5. Confirma
        verify(usuarioRepository, times(1)).findAll();
    }

    // Obtener usuario por id existente
    @Test
    void testObtenerPorId_Exitoso() {
        //1.Prepara los datos
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toResponseDto(usuario)).thenReturn(responseDTO);

        //2. Ejecuta
        UsuarioResponseDTO resultado = usuarioService.obtenerPorId(1L);

        //3. Verifics
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());

        //5. Confirma
        verify(usuarioRepository, times(1)).findById(1L);
    }

    //Obtener usuario por id inexistente
    @Test
    void testObtenerPorId_NoExiste() {
        //1.Prepara los datos
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // 2 Ejecuta y 3 verifica
        assertThrows(UsuarioNotFoundException.class, () -> usuarioService.obtenerPorId(1L));

        //5. Confirma
        verify(usuarioRepository, times(1)).findById(1L);
    }

    //Actualizar usuario exitoso
    @Test
    void testActualizarUsuario_Exitoso() {
        //1.Prepara los datos
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doAnswer(invocation -> {
            UsuarioRequestDTO dto = invocation.getArgument(0);
            Usuario u = invocation.getArgument(1);
            u.setNombre(dto.getNombre());
            return null;
        }).when(usuarioMapper).updateEntityFromDto(eq(requestDTO), eq(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toResponseDto(usuario)).thenReturn(responseDTO);

        //2. Ejecuta
        UsuarioResponseDTO resultado = usuarioService.actualizarUsuario(1L, requestDTO);

        //3. Verifica
        assertEquals("Juan", resultado.getNombre());

        //5. Confirma
        verify(usuarioRepository, times(1)).save(usuario);
    }

    //Eliminar usuario exitoso
    @Test
    void testEliminarUsuario_Exitoso() {
        //1.Prepara los datos
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        //2. Ejecuta
        MessageResponseDTO mensaje = usuarioService.eliminar(1L);

        //3. Verifica
        assertEquals("Usuario con id 1 eliminado exitosamente", mensaje.getMessage());

        //5. Confirma
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    // Eliminar usuario inexistente
    @Test
    void testEliminarUsuario_NoExiste() {
        //1.Prepara los datos
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        //2. Ejecuta y 3 verifica
        assertThrows(UsuarioNotFoundException.class, () -> usuarioService.eliminar(1L));

        //5. Confirma
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }
}
