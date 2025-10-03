package com.bank.management.controller;

import com.bank.management.dto.UsuarioResponseDTO;
import com.bank.management.exception.UsuarioNotFoundException;
import com.bank.management.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    // Caso exitoso: usuario-encontrado

    @Test
    void obtenerUsuarioPorId_Exitoso() throws Exception {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(1L);
        dto.setNombre("Juan Pérez");

        when(usuarioService.obtenerPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));
    }

    // Caso fallido: usuario no existe

    @Test
    void obtenerUsuarioPorId_NoEncontrado() throws Exception {
        when(usuarioService.obtenerPorId(anyLong()))
                .thenThrow(new UsuarioNotFoundException(99L));

        mockMvc.perform(get("/api/usuarios/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Usuario no encontrado"))
                .andExpect(jsonPath("$.message").value("Usuario con id 99 no encontrado"));
    }
}

