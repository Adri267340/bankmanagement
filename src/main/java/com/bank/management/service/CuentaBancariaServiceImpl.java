package com.bank.management.service;

import com.bank.management.dto.CuentaRequestDTO;
import com.bank.management.dto.CuentaResponseDTO;
import com.bank.management.entity.CuentaBancaria;
import com.bank.management.mapper.CuentaBancariaMapper;
import com.bank.management.repository.CuentaBancariaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaBancariaServiceImpl implements CuentaBancariaService {

    private final CuentaBancariaRepository cuentaBancariaRepository;
    private final CuentaBancariaMapper cuentaBancariaMapper;

    public CuentaBancariaServiceImpl(CuentaBancariaRepository cuentaBancariaRepository,
                                     CuentaBancariaMapper cuentaBancariaMapper) {
        this.cuentaBancariaRepository = cuentaBancariaRepository;
        this.cuentaBancariaMapper = cuentaBancariaMapper;
    }

    @Override
    public CuentaResponseDTO guardar(CuentaRequestDTO dto) {
        CuentaBancaria cuenta = cuentaBancariaMapper.toEntity(dto);
        return cuentaBancariaMapper.toDto(cuentaBancariaRepository.save(cuenta));
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
}
