package com.bank.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SaldoInsuficienteException extends RuntimeException {
    private final BigDecimal saldoDisponible;
    private final BigDecimal montoSolicitado;

    public SaldoInsuficienteException(BigDecimal saldoDisponible, BigDecimal montoSolicitado) {
        super(String.format("Intentaste retirar %s pero tu saldo disponible es %s",
                montoSolicitado, saldoDisponible));
        this.saldoDisponible = saldoDisponible;
        this.montoSolicitado = montoSolicitado;
    }

    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
    public BigDecimal getMontoSolicitado() { return montoSolicitado; }
}
