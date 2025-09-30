package com.bank.management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ValidationErrorResponse {
    private int status;
    private String error;
    private List<String> messages;
    private String path;
}






