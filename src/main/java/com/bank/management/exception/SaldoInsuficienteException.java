package com.bank.management.exception;

import java.math.BigDecimal;

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
