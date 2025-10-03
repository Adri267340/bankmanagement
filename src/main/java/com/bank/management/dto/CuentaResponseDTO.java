package com.bank.management.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CuentaResponseDTO {
    private Long id;
    private BigDecimal saldo;
    private Long usuarioId;
    private String tipoCuenta;
}
