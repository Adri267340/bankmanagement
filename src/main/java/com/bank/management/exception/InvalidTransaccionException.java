package com.bank.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTransaccionException extends RuntimeException {

    private final String campo;
    private final String detalle;

    public InvalidTransaccionException(String campo, String detalle) {
        super(String.format("El campo '%s' es inválido: %s", campo, detalle));
        this.campo = campo;
        this.detalle = detalle;
    }

    public String getCampo() {
        return campo;
    }

    public String getDetalle() {
        return detalle;
    }
}

