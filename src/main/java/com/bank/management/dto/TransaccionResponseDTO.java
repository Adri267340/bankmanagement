package com.bank.management.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransaccionResponseDTO {
    private Long id;
    private BigDecimal monto;
    private String tipo;
    private LocalDateTime fecha;
    private Long cuentaId;
}
