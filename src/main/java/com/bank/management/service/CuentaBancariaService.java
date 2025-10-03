package com.bank.management.service;

import com.bank.management.dto.CuentaRequestDTO;
import com.bank.management.dto.CuentaResponseDTO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaBancariaService{

    List<CuentaResponseDTO> obtenerTodas();
    CuentaResponseDTO obtenerPorId(Long id);
    CuentaResponseDTO actualizarCuenta(Long id, CuentaRequestDTO dto);
    void retirar(Long idCuenta, BigDecimal monto);

    @Transactional
    CuentaResponseDTO guardarCuenta(CuentaRequestDTO dto);

    void eliminar(Long id);
}


