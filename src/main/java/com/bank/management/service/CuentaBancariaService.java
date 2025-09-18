package com.bank.management.service;

import com.bank.management.dto.CuentaRequestDTO;
import com.bank.management.dto.CuentaResponseDTO;

import java.util.List;

public interface CuentaBancariaService {
    CuentaResponseDTO guardar(CuentaRequestDTO dto);
    List<CuentaResponseDTO> obtenerTodas();
    CuentaResponseDTO obtenerPorId(Long id);
    void eliminar(Long id);
}

