package com.bank.management.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransaccionRequestDTO {
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor que 0")
    private BigDecimal monto;

    @NotBlank(message = "El tipo es obligatorio (DEPOSITO o RETIRO)")
    private String tipo;

    @NotNull(message = "El id de la cuenta es obligatorio")
    private Long cuentaId;
}

