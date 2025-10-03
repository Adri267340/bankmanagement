package com.bank.management.service;

import com.bank.management.dto.CuentaRequestDTO;
import com.bank.management.dto.CuentaResponseDTO;
import com.bank.management.entity.CuentaBancaria;
import com.bank.management.entity.Usuario;
import com.bank.management.exception.CuentaNotFoundException;
import com.bank.management.exception.SaldoInsuficienteException;
import com.bank.management.exception.UsuarioNotFoundException;
import com.bank.management.mapper.CuentaBancariaMapper;
import com.bank.management.repository.CuentaBancariaRepository;
import com.bank.management.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CuentaBancariaServiceImplTest {

    @Mock
    private CuentaBancariaRepository cuentaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CuentaBancariaMapper mapper;

    private CuentaBancariaServiceImpl service;

    private Usuario usuario;
    private CuentaBancaria cuentaEntity;

    @BeforeEach
    void setUp() {
        service = new CuentaBancariaServiceImpl(cuentaRepository, mapper, usuarioRepository);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Prueba");

        cuentaEntity = new CuentaBancaria();
        cuentaEntity.setId(10L);
        cuentaEntity.setNumeroCuenta("NUM-" + UUID.randomUUID().toString().substring(0,8));
        cuentaEntity.setSaldo(BigDecimal.valueOf(1000));
        cuentaEntity.setTipoCuenta("AHORRO");
        cuentaEntity.setUsuario(usuario);
    }

    //Guardar Caso Exitoso
    @Test
    void guardar_successful() {
        // 1. Datos de entrada
        CuentaRequestDTO req = new CuentaRequestDTO();
        req.setUsuarioId(usuario.getId());
        req.setTipoCuenta("AHORRO");
        req.setSaldoInicial(BigDecimal.valueOf(500));

        // 2. Mocks
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        lenient().when(mapper.toEntity(any(CuentaRequestDTO.class))).thenAnswer(inv -> {
            CuentaBancaria c = new CuentaBancaria();
            c.setTipoCuenta(req.getTipoCuenta());
            c.setSaldo(req.getSaldoInicial());
            return c;
        });
        when(cuentaRepository.save(any(CuentaBancaria.class))).thenAnswer(inv -> {
            CuentaBancaria c = inv.getArgument(0);
            c.setId(100L);
            return c;
        });
        when(mapper.toDto(any(CuentaBancaria.class))).thenAnswer(inv -> {
            CuentaBancaria c = inv.getArgument(0);
            CuentaResponseDTO dto = new CuentaResponseDTO();
            dto.setId(c.getId());
            dto.setSaldo(c.getSaldo());
            dto.setUsuarioId(c.getUsuario() != null ? c.getUsuario().getId() : req.getUsuarioId());
            return dto;
        });

        // 3. Llamada al método
        CuentaResponseDTO result = service.guardarCuenta(req);

        // 4. Verificaciones de resultado
        assertNotNull(result);
        assertEquals(100L, result.getId().longValue());
        assertEquals(req.getSaldoInicial(), result.getSaldo());

        // 5. Verificar interacciones
        verify(usuarioRepository).findById(usuario.getId());
        verify(cuentaRepository).save(any(CuentaBancaria.class));
        verify(mapper).toDto(any(CuentaBancaria.class));
    }

    //Guardar Caso fallido
    @Test
    void guardar_failed_usuarioNoExiste() {
        CuentaRequestDTO req = new CuentaRequestDTO();
        req.setUsuarioId(999L);
        req.setTipoCuenta("CORRIENTE");
        req.setSaldoInicial(BigDecimal.valueOf(0));

        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class, () -> service.guardarCuenta(req));

        verify(usuarioRepository).findById(999L);
        verifyNoInteractions(cuentaRepository);
    }

   //Obtener todos Caso exitoso
    @Test
    void obtenerTodas_successful() {
        // 1. Datos no necesarios de entrada
        // 2. Mocks: repo devuelve lista con dos entidades
        CuentaBancaria c2 = new CuentaBancaria();
        c2.setId(11L);
        c2.setSaldo(BigDecimal.valueOf(200));
        c2.setUsuario(usuario);

        when(cuentaRepository.findAll()).thenReturn(List.of(cuentaEntity, c2));
        when(mapper.toDto(any(CuentaBancaria.class))).thenAnswer(inv -> {
            CuentaBancaria c = inv.getArgument(0);
            CuentaResponseDTO dto = new CuentaResponseDTO();
            dto.setId(c.getId());
            dto.setSaldo(c.getSaldo());
            dto.setUsuarioId(c.getUsuario().getId());
            return dto;
        });

        // 3. Llamada
        var lista = service.obtenerTodas();

        // 4. Verificaciones
        assertNotNull(lista);
        assertEquals(2, lista.size());

        // 5. Interacciones
        verify(cuentaRepository).findAll();
        verify(mapper, times(2)).toDto(any(CuentaBancaria.class));
    }

    //Obtener por id Caso exitoso
    @Test
    void obtenerPorId_successful() {
        when(cuentaRepository.findById(10L)).thenReturn(Optional.of(cuentaEntity));
        when(mapper.toDto(any(CuentaBancaria.class))).thenAnswer(inv -> {
            CuentaBancaria c = inv.getArgument(0);
            CuentaResponseDTO dto = new CuentaResponseDTO();
            dto.setId(c.getId());
            dto.setSaldo(c.getSaldo());
            dto.setUsuarioId(c.getUsuario().getId());
            return dto;
        });

        CuentaResponseDTO dto = service.obtenerPorId(10L);

        assertNotNull(dto);
        assertEquals(10L, dto.getId().longValue());
        verify(cuentaRepository).findById(10L);
        verify(mapper).toDto(any(CuentaBancaria.class));
    }

    //Obtener Caso fallido
    @Test
    void obtenerPorId_failed_notFound() {
        when(cuentaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CuentaNotFoundException.class, () -> service.obtenerPorId(99L));

        verify(cuentaRepository).findById(99L);
        verifyNoMoreInteractions(mapper);
    }

    //Actualizar caso exitoso
    @Test
    void actualizar_successful() {
        // 1. Datos de entrada
        CuentaRequestDTO req = new CuentaRequestDTO();
        req.setUsuarioId(usuario.getId());
        req.setTipoCuenta("CORRIENTE");
        req.setSaldoInicial(BigDecimal.valueOf(300));

        // 2. Mocks
        CuentaBancaria existente = new CuentaBancaria();
        existente.setId(10L);
        existente.setSaldo(BigDecimal.valueOf(100));
        existente.setUsuario(usuario);
        when(cuentaRepository.findById(10L)).thenReturn(Optional.of(existente));

        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        doAnswer(inv -> {
            CuentaRequestDTO dto = inv.getArgument(0);
            CuentaBancaria ent = inv.getArgument(1);
            ent.setTipoCuenta(dto.getTipoCuenta());
            ent.setSaldo(dto.getSaldoInicial());
            return null;
        }).when(mapper).updateEntity(any(CuentaRequestDTO.class), any(CuentaBancaria.class));
        when(cuentaRepository.save(any(CuentaBancaria.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDto(any(CuentaBancaria.class))).thenAnswer(inv -> {
            CuentaBancaria c = inv.getArgument(0);
            CuentaResponseDTO r = new CuentaResponseDTO();
            r.setId(c.getId());
            r.setSaldo(c.getSaldo());
            r.setUsuarioId(c.getUsuario().getId());
            return r;
        });

        // 3. Llamada
        CuentaResponseDTO updated = service.actualizarCuenta(10L, req);

        // 4. Verificar
        assertNotNull(updated);
        assertEquals(BigDecimal.valueOf(300), updated.getSaldo());

        // 5. Interacciones
        verify(cuentaRepository).findById(10L);
        verify(mapper).updateEntity(any(CuentaRequestDTO.class), any(CuentaBancaria.class));
        verify(cuentaRepository).save(any(CuentaBancaria.class));
    }

    //Retirar caso exitoso
    @Test
    void retirar_successful() {
        when(cuentaRepository.findById(10L)).thenReturn(Optional.of(cuentaEntity));

        // 1. Datos entrada
        BigDecimal monto = BigDecimal.valueOf(500);

        // 3. llamada
        service.retirar(10L, monto);

        // 4. Verificar estado: saldo reducido
        assertEquals(BigDecimal.valueOf(500), cuentaEntity.getSaldo()); // 1000 - 500

        // 5. Interacciones
        verify(cuentaRepository).findById(10L);
        verify(cuentaRepository).save(cuentaEntity);
    }

   //Retirar caso fallido
    @Test
    void retirar_failed_insufficient() {
        when(cuentaRepository.findById(10L)).thenReturn(Optional.of(cuentaEntity));

        BigDecimal monto = BigDecimal.valueOf(2000);

        SaldoInsuficienteException ex = assertThrows(SaldoInsuficienteException.class,
                () -> service.retirar(10L, monto));
        assertTrue(ex.getMessage().contains("Intentaste retirar"));

        verify(cuentaRepository).findById(10L);
        verify(cuentaRepository, never()).save(any());
    }

    //Retirar cuenta No existente Caso fallido
    @Test
    void retirar_failed_cuentaNotFound() {
        when(cuentaRepository.findById(55L)).thenReturn(Optional.empty());

        assertThrows(CuentaNotFoundException.class, () -> service.retirar(55L, BigDecimal.TEN));

        verify(cuentaRepository).findById(55L);
        verify(cuentaRepository, never()).save(any());
    }

    //Eliminar Caso exitoso
    @Test
    void eliminar_successful() {
        when(cuentaRepository.existsById(10L)).thenReturn(true);
        doNothing().when(cuentaRepository).deleteById(10L);

        service.eliminar(10L);

        verify(cuentaRepository).existsById(10L);
        verify(cuentaRepository).deleteById(10L);
    }

    //Eliminar Caso fallido
    @Test
    void eliminar_failed_notFound() {
        when(cuentaRepository.existsById(99L)).thenReturn(false);

        assertThrows(CuentaNotFoundException.class, () -> service.eliminar(99L));

        verify(cuentaRepository).existsById(99L);
        verify(cuentaRepository, never()).deleteById(anyLong());
    }
}
