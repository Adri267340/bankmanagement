package com.bank.management.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransaccionRequestDTO {
    private BigDecimal monto;
    private String tipo;
    private Long cuentaId; // aquí solo guardamos la referencia a la cuenta
}

