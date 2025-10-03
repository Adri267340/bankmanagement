package com.bank.management.exception;

public class TransaccionNotFoundException extends RuntimeException {

    private final Long transaccionId;

    public TransaccionNotFoundException(Long transaccionId) {
        super(String.format("Transacción con id %d no se encontró", transaccionId));
        this.transaccionId = transaccionId;
    }

    public Long getTransaccionId() {
        return transaccionId;
    }
}

