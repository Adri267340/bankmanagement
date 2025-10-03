package com.bank.management.service;

import com.bank.management.dto.TransaccionRequestDTO;
import com.bank.management.dto.TransaccionResponseDTO;
import com.bank.management.entity.CuentaBancaria;
import com.bank.management.entity.Transaccion;
import com.bank.management.exception.*;
import com.bank.management.mapper.TransaccionMapper;
import com.bank.management.repository.CuentaBancariaRepository;
import com.bank.management.repository.TransaccionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CuentaBancariaRepository cuentaBancariaRepository;
    private final TransaccionMapper transaccionMapper;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository,
                                  CuentaBancariaRepository cuentaBancariaRepository,
                                  TransaccionMapper transaccionMapper) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.transaccionMapper = transaccionMapper;
    }

    @Override
    @Transactional
    public TransaccionResponseDTO crear(TransaccionRequestDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body de la petición es obligatorio");
        }

        // 🔹 Lista para acumular errores
        List<String> errores = new ArrayList<>();

        if (dto.getMonto() == null || dto.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            errores.add("El campo 'monto' es inválido: Debe ser mayor a 0");
        }

        if (dto.getTipo() == null || dto.getTipo().isBlank()) {
            errores.add("El campo 'tipo' es inválido: Debe ser DEPOSITO o RETIRO");
        }

        if (dto.getCuentaId() == null) {
            errores.add("El campo 'cuentaId' no puede ser nulo");
        }

        // 👉 si hay errores, lanza excepción con todos juntos
        if (!errores.isEmpty()) {
            throw new MultipleInvalidTransaccionException(errores);
        }

        // 🔹 Buscar cuenta
        CuentaBancaria cuenta = cuentaBancariaRepository.findById(dto.getCuentaId())
                .orElseThrow(() -> new CuentaNotFoundException(dto.getCuentaId()));

        // 🔹 Normalizar tipo
        String tipo = dto.getTipo().trim().toUpperCase(Locale.ROOT);

        if (!"DEPOSITO".equals(tipo) && !"RETIRO".equals(tipo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo inválido: use DEPOSITO o RETIRO");
        }

        // 🔹 Lógica de la transacción
        if ("DEPOSITO".equals(tipo)) {
            cuenta.setSaldo(cuenta.getSaldo().add(dto.getMonto()));
        } else { // RETIRO
            if (cuenta.getSaldo().compareTo(dto.getMonto()) < 0) {
                throw new SaldoInsuficienteException(cuenta.getSaldo(), dto.getMonto());
            }
            cuenta.setSaldo(cuenta.getSaldo().subtract(dto.getMonto()));
        }

        cuentaBancariaRepository.save(cuenta);

        // 🔹 Crear transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setMonto(dto.getMonto());
        transaccion.setTipo(tipo);
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setCuenta(cuenta);

        Transaccion saved = transaccionRepository.save(transaccion);

        return transaccionMapper.toDto(saved);
    }

    @Override
    public List<TransaccionResponseDTO> obtenerTodos() {
        return transaccionRepository.findAll()
                .stream()
                .map(transaccionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransaccionResponseDTO obtenerPorId(Long id) {
        Transaccion transaccion = transaccionRepository.findById(id)
                .orElseThrow(() -> new TransaccionNotFoundException(id));
        return transaccionMapper.toDto(transaccion);
    }

    @Override
    public void eliminar(Long id) {
        Transaccion existente = transaccionRepository.findById(id)
                .orElseThrow(() -> new TransaccionNotFoundException(id));
        transaccionRepository.delete(existente);
    }


    @Override
    public TransaccionResponseDTO actualizarTransaccion(Long id, TransaccionRequestDTO dto) {
        Transaccion existente = transaccionRepository.findById(id)
                .orElseThrow(() -> new TransaccionNotFoundException(id));
        transaccionMapper.updateEntity(dto, existente);
        Transaccion actualizada = transaccionRepository.save(existente);
        return transaccionMapper.toDto(actualizada);
    }
}
