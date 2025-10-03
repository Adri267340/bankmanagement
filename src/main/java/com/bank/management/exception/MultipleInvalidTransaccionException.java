package com.bank.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MultipleInvalidTransaccionException extends RuntimeException {
    private final List<String> errores;

    public MultipleInvalidTransaccionException(List<String> errores) {
        super("Errores de validación en la transacción");
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}

