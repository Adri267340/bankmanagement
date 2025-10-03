package com.bank.management.service;

import com.bank.management.dto.CuentaRequestDTO;
import com.bank.management.dto.CuentaResponseDTO;
import com.bank.management.entity.CuentaBancaria;
import com.bank.management.entity.Usuario;
import com.bank.management.exception.*;
import com.bank.management.mapper.CuentaBancariaMapper;
import com.bank.management.repository.CuentaBancariaRepository;
import com.bank.management.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CuentaBancariaServiceImpl implements CuentaBancariaService {

    private final CuentaBancariaRepository cuentaBancariaRepository;
    private final CuentaBancariaMapper cuentaBancariaMapper;
    private final UsuarioRepository usuarioRepository;

    public CuentaBancariaServiceImpl(CuentaBancariaRepository cuentaBancariaRepository,
                                     CuentaBancariaMapper cuentaBancariaMapper,
                                     UsuarioRepository usuarioRepository) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.cuentaBancariaMapper = cuentaBancariaMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public CuentaResponseDTO guardarCuenta(CuentaRequestDTO dto) {
        // 1. Buscar usuario
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new UsuarioNotFoundException(dto.getUsuarioId()));

        // 2. Validar duplicado
        if (cuentaBancariaRepository.existsByUsuarioIdAndTipoCuenta(dto.getUsuarioId(), dto.getTipoCuenta())) {
            throw new DuplicatedDataException(
                    "Cuenta",
                    "El usuario con id " + dto.getUsuarioId() +
                            " ya tiene una cuenta de tipo " + dto.getTipoCuenta()
            );
        }

        // 3. Generar número único
        String numero;
        do {
            numero = generarNumeroCuenta();
        } while (cuentaBancariaRepository.existsByNumeroCuenta(numero));

        // 4. Crear cuenta
        CuentaBancaria cuenta = new CuentaBancaria();
        cuenta.setUsuario(usuario);
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setSaldo(dto.getSaldoInicial());
        cuenta.setNumeroCuenta(numero);

        cuenta = cuentaBancariaRepository.save(cuenta);

        // 5. Retornar DTO usando el mapper
        return cuentaBancariaMapper.toDto(cuenta);
    }



    @Override
    public List<CuentaResponseDTO> obtenerTodas() {
        return cuentaBancariaRepository.findAll()
                .stream()
                .map(cuentaBancariaMapper::toDto)
                .toList();
    }

    @Override
    public CuentaResponseDTO obtenerPorId(Long id) {
        return cuentaBancariaRepository.findById(id)
                .map(cuentaBancariaMapper::toDto)
                .orElseThrow(() -> new CuentaNotFoundException(id));
    }

    @Override
    public CuentaResponseDTO actualizarCuenta(Long id, CuentaRequestDTO dto) {
        CuentaBancaria existente = cuentaBancariaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException(id));

        cuentaBancariaMapper.updateEntity(dto, existente);
        if(dto.getUsuarioId()!=null) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
        .orElseThrow(() -> new UsuarioNotFoundException(dto.getUsuarioId()));
        existente.setUsuario(usuario);
        }

        CuentaBancaria actualizada =
                cuentaBancariaRepository.save(existente);
        return cuentaBancariaMapper.toDto(actualizada);
    }


    @Override
    public void retirar(Long idCuenta, BigDecimal monto) {
        CuentaBancaria cuenta = cuentaBancariaRepository.findById(idCuenta)
                .orElseThrow(() -> new CuentaNotFoundException(idCuenta));

        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        if (cuenta.getSaldo().compareTo(monto) < 0) {
            throw new SaldoInsuficienteException(cuenta.getSaldo(), monto);
        }

        cuenta.setSaldo(cuenta.getSaldo().subtract(monto));
        cuentaBancariaRepository.save(cuenta);
    }

    @Override
    public void eliminar(Long id) {
        if (!cuentaBancariaRepository.existsById(id)) {
            throw new CuentaNotFoundException(id);
        }
        cuentaBancariaRepository.deleteById(id);
    }

    private String generarNumeroCuenta() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
