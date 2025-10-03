package com.bank.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDTO {
    private String message;
    private Long id;

    public MessageResponseDTO(String message) {
        this.message = message;
    }
}


