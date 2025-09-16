package com.bank.management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDTO {
    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @Email(message = "El email debe ser valido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacia")
    private String password;
}



