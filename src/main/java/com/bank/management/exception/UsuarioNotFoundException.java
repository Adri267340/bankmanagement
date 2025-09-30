package com.bank.management.exception;

public class UsuarioNotFoundException extends RuntimeException {
    private final Long usuarioId;

    public UsuarioNotFoundException(Long usuarioId) {
        super(String.format("Usuario con id %d no encontrado", usuarioId));
        this.usuarioId = usuarioId;
    }

    public Long getUsuarioId() { return usuarioId; }
}


