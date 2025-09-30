package com.bank.management.service;

import com.bank.management.dto.UsuarioRequestDTO;
import com.bank.management.dto.UsuarioResponseDTO;
import com.bank.management.entity.Usuario;
import com.bank.management.exception.UsuarioNotFoundException;
import com.bank.management.mapper.UsuarioMapper;
import com.bank.management.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuarioTest;
    private UsuarioRequestDTO requestDTO;
    private UsuarioResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        usuarioTest = new Usuario();
        usuarioTest.setId(1L);
        usuarioTest.setNombre("Adriana");

        requestDTO = new UsuarioRequestDTO();
        requestDTO.setNombre("Adriana");

        responseDTO = new UsuarioResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNombre("Adriana");
    }

    // ----------- GUARDAR (positivo) -----------
    @Test
    void guardar_successful() {
        Mockito.when(usuarioMapper.toEntity(any(UsuarioRequestDTO.class))).thenReturn(usuarioTest);
        Mockito.when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioTest);
        Mockito.when(usuarioMapper.toResponseDto(any(Usuario.class))).thenReturn(responseDTO);

        var result = usuarioService.guardar(requestDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Adriana", result.getNombre());
        Mockito.verify(usuarioRepository).save(any(Usuario.class));
    }

    // ----------- OBTENER POR ID (negativo) -----------
    @Test
    void obtenerPorId_failed_noExiste() {
        Mockito.when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class, () -> usuarioService.obtenerPorId(99L));
        Mockito.verify(usuarioRepository).findById(99L);
    }
}
