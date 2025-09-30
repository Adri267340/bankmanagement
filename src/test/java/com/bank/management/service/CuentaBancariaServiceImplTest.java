package com.bank.management.service;

import com.bank.management.dto.CuentaRequestDTO;
import com.bank.management.dto.CuentaResponseDTO;
import com.bank.management.entity.CuentaBancaria;
import com.bank.management.entity.Usuario;
import com.bank.management.exception.UsuarioNotFoundException;
import com.bank.management.mapper.CuentaBancariaMapper;
import com.bank.management.repository.CuentaBancariaRepository;
import com.bank.management.repository.UsuarioRepository;
import com.bank.management.service.CuentaBancariaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CuentaBancariaServiceImplTest {

    @Mock
    private CuentaBancariaRepository cuentaBancariaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private CuentaBancariaMapper cuentaBancariaMapper;

    private CuentaBancariaService cuentaBancariaService;
    private CuentaBancaria cuentaTest;
    private Usuario usuarioTest;

    @BeforeEach
    void setUp() {
        cuentaBancariaService = new CuentaBancariaServiceImpl(
                cuentaBancariaRepository,
                cuentaBancariaMapper,
                usuarioRepository
        );

        usuarioTest = new Usuario();
        usuarioTest.setId(1L);
        usuarioTest.setNombre("Adriana");

        cuentaTest = new CuentaBancaria();
        cuentaTest.setId(1L);
        cuentaTest.setUsuario(usuarioTest);
        cuentaTest.setSaldo(java.math.BigDecimal.valueOf(1000));
        cuentaTest.setNumeroCuenta("123456789012");
    }

    //Guardar
    @Test
    void guardar_successful() {
        CuentaRequestDTO request = new CuentaRequestDTO();
        request.setUsuarioId(1L);

        CuentaResponseDTO response = new CuentaResponseDTO();
        response.setId(1L);

        Mockito.when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioTest));
        Mockito.when(cuentaBancariaMapper.toEntity(any(CuentaRequestDTO.class))).thenReturn(cuentaTest);
        Mockito.when(cuentaBancariaRepository.existsByNumeroCuenta(any(String.class))).thenReturn(false);
        Mockito.when(cuentaBancariaRepository.save(any(CuentaBancaria.class))).thenReturn(cuentaTest);
        Mockito.when(cuentaBancariaMapper.toDto(any(CuentaBancaria.class))).thenReturn(response);

        var result = cuentaBancariaService.guardar(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        Mockito.verify(usuarioRepository).findById(1L);
        Mockito.verify(cuentaBancariaRepository).save(any(CuentaBancaria.class));
    }

    //Guardar fallido
    @Test
    void guardar_failed_usuarioNoExiste() {
        CuentaRequestDTO request = new CuentaRequestDTO();
        request.setUsuarioId(99L);

        Mockito.when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class, () -> cuentaBancariaService.guardar(request));
        Mockito.verify(usuarioRepository).findById(99L);
    }

    // obtener por ID
    @Test
    void obtenerPorId_successful() {
        CuentaResponseDTO response = new CuentaResponseDTO();
        response.setId(1L);

        Mockito.when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.of(cuentaTest));
        Mockito.when(cuentaBancariaMapper.toDto(any(CuentaBancaria.class))).thenReturn(response);

        var result = cuentaBancariaService.obtenerPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        Mockito.verify(cuentaBancariaRepository).findById(1L);
        Mockito.verify(cuentaBancariaMapper).toDto(any(CuentaBancaria.class));
    }

    //obtener fallida
    @Test
    void obtenerPorId_failed_noExiste() {
        Mockito.when(cuentaBancariaRepository.findById(2L)).thenReturn(Optional.empty());

        var result = cuentaBancariaService.obtenerPorId(2L);

        assertNull(result);
        Mockito.verify(cuentaBancariaRepository).findById(2L);
    }

    // Obtener todas
    @Test
    void obtenerTodas_successful() {
        CuentaResponseDTO response = new CuentaResponseDTO();
        response.setId(1L);

        Mockito.when(cuentaBancariaRepository.findAll()).thenReturn(List.of(cuentaTest, cuentaTest));
        Mockito.when(cuentaBancariaMapper.toDto(any(CuentaBancaria.class))).thenReturn(response);

        var result = cuentaBancariaService.obtenerTodas();

        assertEquals(2, result.size());
        Mockito.verify(cuentaBancariaRepository).findAll();
        Mockito.verify(cuentaBancariaMapper, Mockito.times(2)).toDto(any(CuentaBancaria.class));
    }

    //obrener todas fallido
    @Test
    void obtenerTodas_failed_vacia() {
        Mockito.when(cuentaBancariaRepository.findAll()).thenReturn(new ArrayList<>());

        var result = cuentaBancariaService.obtenerTodas();

        assertEquals(0, result.size());
        Mockito.verify(cuentaBancariaRepository).findAll();
        Mockito.verifyNoMoreInteractions(cuentaBancariaMapper);
    }
}
