package com.bank.management.exception;

public class CuentaNotFoundException extends RuntimeException {
    private final Long cuentaId;

    public CuentaNotFoundException(Long cuentaId) {
        super(String.format("Cuenta bancaria con id %d no encontrada", cuentaId));
        this.cuentaId = cuentaId;
    }

    public Long getCuentaId() { return cuentaId; }
}

