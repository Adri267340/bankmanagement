package com.bank.management.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RetiroRequestDTO {
    @NotNull(message = "Debes indicar el id de la cuenta ")
    private Long cuentaId;

    @NotNull(message = "Debes editar el monto a retirar")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor que 0")
    private BigDecimal monto;
}


