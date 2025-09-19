package com.bank.management.service;

import com.bank.management.dto.CuentaRequestDTO;
import com.bank.management.dto.CuentaResponseDTO;
import com.bank.management.entity.CuentaBancaria;
import com.bank.management.entity.Usuario;
import com.bank.management.mapper.CuentaBancariaMapper;
import com.bank.management.repository.CuentaBancariaRepository;
import com.bank.management.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public CuentaResponseDTO guardar(CuentaRequestDTO dto) {
        if (dto == null || dto.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarioId es obligatorio");
        }

        // 1. Buscar usuario
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // 2. Crear cuenta desde mapper (saldo y usuario)
        CuentaBancaria cuenta = cuentaBancariaMapper.toEntity(usuario);

        // 3. Generar numeroCuenta único (12 chars) y asignar
        String numero;
        do {
            numero = generarNumeroCuenta();
        } while (cuentaBancariaRepository.existsByNumeroCuenta(numero));
        cuenta.setNumeroCuenta(numero);

        // 4. Guardar y devolver DTO
        CuentaBancaria saved = cuentaBancariaRepository.save(cuenta);
        return cuentaBancariaMapper.toDto(saved);
    }

    @Override
    public List<CuentaResponseDTO> obtenerTodas() {
        return cuentaBancariaRepository.findAll()
                .stream()
                .map(cuentaBancariaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaResponseDTO obtenerPorId(Long id) {
        return cuentaBancariaRepository.findById(id)
                .map(cuentaBancariaMapper::toDto)
                .orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        cuentaBancariaRepository.deleteById(id);
    }

    // Helper
    private String generarNumeroCuenta() {
        // genera 12 caracteres hex sin guiones (puedes ajustar longitud 10-20)
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}

