package com.bank.management.mapper;

import com.bank.management.dto.CuentaResponseDTO;
import com.bank.management.entity.CuentaBancaria;
import com.bank.management.entity.Usuario;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CuentaBancariaMapper {

    public CuentaBancaria toEntity(Usuario usuario) {
        CuentaBancaria cuenta = new CuentaBancaria();
        cuenta.setSaldo(BigDecimal.ZERO);
        cuenta.setUsuario(usuario);
        return cuenta;
    }

    public CuentaResponseDTO toDto(CuentaBancaria cuenta) {
        CuentaResponseDTO dto = new CuentaResponseDTO();
        dto.setId(cuenta.getId());
        dto.setSaldo(cuenta.getSaldo() != null ? cuenta.getSaldo() : BigDecimal.ZERO);
        if (cuenta.getUsuario() != null) {
            dto.setUsuarioId(cuenta.getUsuario().getId());
        }
        return dto;
    }
}
