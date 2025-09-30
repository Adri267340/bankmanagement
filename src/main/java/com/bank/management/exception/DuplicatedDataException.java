package com.bank.management.exception;

public class DuplicatedDataException extends RuntimeException {
    private final String entity;
    private final String fieldValue;

    public DuplicatedDataException(String entity, String fieldValue) {
        super(String.format("%s con valor '%s' ya existe", entity, fieldValue));
        this.entity = entity;
        this.fieldValue = fieldValue;
    }

    public String getEntity() { return entity; }
    public String getFieldValue() { return fieldValue; }
}

