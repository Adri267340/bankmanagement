package com.bank.management.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransaccionRequestDTO {
    @NotNull(message = "El id de la cuenta es obligatorio")
    private Long cuentaId;

    @NotNull(message = "El monto es obligatorio")
    private BigDecimal monto;

    @NotNull(message = "El tipo de transaccion es obligatorio")
    private String tipo; //Deposito o Retiro
}
