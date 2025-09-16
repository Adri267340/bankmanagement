package com.bank.management.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransaccionResponseDTO {
    private long id;
    private String tipo;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private Long cuentaId;
}
