package com.bank.management.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CuentaRequestDTO {
    @NotNull(message = "El id del usuario es obligatorio")
    private Long UsuarioId;
}
