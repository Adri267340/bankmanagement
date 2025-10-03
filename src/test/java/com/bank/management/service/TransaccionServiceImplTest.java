package com.bank.management.service;

import com.bank.management.dto.TransaccionRequestDTO;
import com.bank.management.dto.TransaccionResponseDTO;
import com.bank.management.entity.CuentaBancaria;
import com.bank.management.entity.Transaccion;
import com.bank.management.exception.*;
import com.bank.management.mapper.TransaccionMapper;
import com.bank.management.repository.CuentaBancariaRepository;
import com.bank.management.repository.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    private CuentaBancaria cuenta;
    private Transaccion transaccion;
    private TransaccionRequestDTO requestDTO;
    private TransaccionResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        cuenta = new CuentaBancaria();
        cuenta.setId(1L);
        cuenta.setSaldo(BigDecimal.valueOf(100));

        transaccion = new Transaccion();
        transaccion.setId(1L);
        transaccion.setMonto(BigDecimal.valueOf(50));
        transaccion.setTipo("DEPOSITO");
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setCuenta(cuenta);

        requestDTO = new TransaccionRequestDTO();
        requestDTO.setCuentaId(1L);
        requestDTO.setMonto(BigDecimal.valueOf(50));
        requestDTO.setTipo("DEPOSITO");

        responseDTO = new TransaccionResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setMonto(BigDecimal.valueOf(50));
        responseDTO.setTipo("DEPOSITO");
    }


    //Crear Deposito Caso exitoso
    @Test
    void crear_successful_deposito() {
        //1.Datos de entrada
        when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);
        when(transaccionMapper.toDto(any(Transaccion.class))).thenReturn(responseDTO);

        //2.Método bajo prueba
        TransaccionResponseDTO result = transaccionService.crear(requestDTO);

        //3.Verificaciones
        assertNotNull(result);
        assertEquals("DEPOSITO", result.getTipo());
        assertEquals(BigDecimal.valueOf(150), cuenta.getSaldo()); // saldo actualizado

        verify(cuentaBancariaRepository).save(cuenta);
        verify(transaccionRepository).save(any(Transaccion.class));
        verify(transaccionMapper).toDto(any(Transaccion.class));
    }

    //Crear retiro Caso exitoso
    @Test
    void crear_successful_retiro() {
        //1. Datos de entrada
        requestDTO.setTipo("RETIRO");

        TransaccionResponseDTO retiroResponse = new TransaccionResponseDTO();
        retiroResponse.setId(1L);
        retiroResponse.setMonto(BigDecimal.valueOf(50));
        retiroResponse.setTipo("RETIRO");

        when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);
        when(transaccionMapper.toDto(any(Transaccion.class))).thenReturn(retiroResponse);

        //2.Método bajo prueba
        TransaccionResponseDTO result = transaccionService.crear(requestDTO);

        //3.Verificaciones
        assertNotNull(result);
        assertEquals("RETIRO", result.getTipo());
        assertEquals(BigDecimal.valueOf(50), cuenta.getSaldo());
    }


    //Crear monto invalido Caso fallido
    @Test
    void crear_fails_whenMontoInvalido() {
        //1.Datos inválidos
        requestDTO.setMonto(BigDecimal.ZERO);

        //2.Método bajo prueba + Verificación
        MultipleInvalidTransaccionException ex = assertThrows(
                MultipleInvalidTransaccionException.class,
                () -> transaccionService.crear(requestDTO)
        );

        assertTrue(ex.getErrores().contains("El campo 'monto' es inválido: Debe ser mayor a 0"));
    }

    //Crear cuenta no existente Caso fallido
    @Test
    void crear_fails_whenCuentaNoExiste() {
        //1.Mock: cuenta no encontrada
        when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.empty());

        //2.Método bajo prueba + Verificación
        assertThrows(CuentaNotFoundException.class, () -> transaccionService.crear(requestDTO));
    }

    //Crear retiro Saldo insuficiente Caso fallido
    @Test
    void crear_fails_whenSaldoInsuficiente() {
        //1.Datos inválidos
        requestDTO.setTipo("RETIRO");
        requestDTO.setMonto(BigDecimal.valueOf(200));
        when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        //2.Método bajo prueba + Verificación
        assertThrows(SaldoInsuficienteException.class, () -> transaccionService.crear(requestDTO));
    }

    //Crear transferencia caso fallido
    @Test
    void crear_fails_whenTipoInvalido() {
        //1. Tipo inválido
        requestDTO.setTipo("TRANSFERENCIA");
        when(cuentaBancariaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        //2.Método bajo prueba + Verificación
        assertThrows(ResponseStatusException.class, () -> transaccionService.crear(requestDTO));
    }

    //Obtener todos Caso exitoso
    @Test
    void obtenerTodos_successful() {
        //1.Datos de entrada
        when(transaccionRepository.findAll()).thenReturn(List.of(transaccion));
        when(transaccionMapper.toDto(transaccion)).thenReturn(responseDTO);

        //2. Método bajo prueba
        List<TransaccionResponseDTO> result = transaccionService.obtenerTodos();

        //3. Verificaciones
        assertEquals(1, result.size());
        assertEquals("DEPOSITO", result.get(0).getTipo());
        verify(transaccionRepository).findAll();
    }

   //Obtener por id Caso exitoso
    @Test
    void obtenerPorId_successful() {
        //1. Datos de entrada
        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccion));
        when(transaccionMapper.toDto(transaccion)).thenReturn(responseDTO);

        //2. Método bajo prueba
        TransaccionResponseDTO result = transaccionService.obtenerPorId(1L);

        //3. Verificaciones
        assertNotNull(result);
        assertEquals("DEPOSITO", result.getTipo());
        verify(transaccionRepository).findById(1L);
    }

    //Obtener por id Caso fallido
    @Test
    void obtenerPorId_fails_whenNotFound() {
        //1.Mock: transacción no existe
        when(transaccionRepository.findById(1L)).thenReturn(Optional.empty());

        //2.Método bajo prueba + Verificación
        assertThrows(TransaccionNotFoundException.class, () -> transaccionService.obtenerPorId(1L));
    }

    //Actualizar Caso exitoso
    @Test
    void actualizar_successful() {
        //1. Datos de entrada
        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccion));
        when(transaccionRepository.save(transaccion)).thenReturn(transaccion);
        when(transaccionMapper.toDto(transaccion)).thenReturn(responseDTO);

        //2.Método bajo prueba
        TransaccionResponseDTO result = transaccionService.actualizarTransaccion(1L, requestDTO);

        //3.Verificaciones
        assertNotNull(result);
        verify(transaccionRepository).save(transaccion);
        verify(transaccionMapper).updateEntity(requestDTO, transaccion);
    }

    //  Actualizar Caso fallido
    @Test
    void actualizar_fails_whenNotFound() {
        //1.Mock: transacción no existe
        when(transaccionRepository.findById(1L)).thenReturn(Optional.empty());

        //2.Método bajo prueba + Verificación
        assertThrows(TransaccionNotFoundException.class,
                () -> transaccionService.actualizarTransaccion(1L, requestDTO));
    }


    //Eliminar caso Exitoso
    @Test
    void eliminar_successful() {
        //1. Datos de entrada
        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccion));

        //2.Método bajo prueba
        transaccionService.eliminar(1L);

        //3.Verificaciones
        verify(transaccionRepository).delete(transaccion);
    }

    //Eliminar caso fallido
    @Test
    void eliminar_fails_whenNotFound() {
        //1.Mock: transacción no existe
        when(transaccionRepository.findById(1L)).thenReturn(Optional.empty());

        //2.Método bajo prueba + Verificación
        assertThrows(TransaccionNotFoundException.class, () -> transaccionService.eliminar(1L));
    }
}



