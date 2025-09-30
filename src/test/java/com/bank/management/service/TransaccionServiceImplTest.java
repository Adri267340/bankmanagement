package com.bank.management.service;

import com.bank.management.dto.TransaccionRequestDTO;
import com.bank.management.dto.TransaccionResponseDTO;
import com.bank.management.entity.CuentaBancaria;
import com.bank.management.entity.Transaccion;
import com.bank.management.mapper.TransaccionMapper;
import com.bank.management.repository.CuentaBancariaRepository;
import com.bank.management.repository.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceImplTest {

    @Mock
    private TransaccionRepository transaccionRepository;
    @Mock
    private CuentaBancariaRepository cuentaBancariaRepository;
    @Mock
    private TransaccionMapper transaccionMapper;

    @InjectMocks
    private TransaccionServiceImpl transaccionService;

    private CuentaBancaria cuentaTest;

    @BeforeEach
    void setUp() {
        cuentaTest = new CuentaBancaria();
        cuentaTest.setId(1L);
        cuentaTest.setSaldo(BigDecimal.valueOf(1000));
        cuentaTest.setNumeroCuenta("123456789012");
    }

    // Crear Transaccion
    @Test
    void crear_successful_deposito() {
        TransaccionRequestDTO request = new TransaccionRequestDTO();
        request.setCuentaId(1L);
        request.setMonto(BigDecimal.valueOf(500));
        request.setTipo("DEPOSITO");

        Transaccion transaccion = new Transaccion();
        transaccion.setId(1L);

        TransaccionResponseDTO response = new TransaccionResponseDTO();
        response.setId(1L);

        Mockito.when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.of(cuentaTest));
        Mockito.when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);
        Mockito.when(transaccionMapper.toDto(any(Transaccion.class))).thenReturn(response);

        var result = transaccionService.crear(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(BigDecimal.valueOf(1500), cuentaTest.getSaldo());
        Mockito.verify(transaccionRepository).save(any(Transaccion.class));
    }

    // Crear Transaccion fallida
    @Test
    void crear_failed_saldoInsuficiente() {
        TransaccionRequestDTO request = new TransaccionRequestDTO();
        request.setCuentaId(1L);
        request.setMonto(BigDecimal.valueOf(2000)); // más de lo que tiene
        request.setTipo("RETIRO");

        Mockito.when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.of(cuentaTest));

        assertThrows(ResponseStatusException.class, () -> transaccionService.crear(request));
        assertEquals(BigDecimal.valueOf(1000), cuentaTest.getSaldo()); // saldo no cambia
        Mockito.verify(transaccionRepository, Mockito.never()).save(any(Transaccion.class));
    }
}
