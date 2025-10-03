package com.bank.management.controller;

import com.bank.management.dto.TransaccionRequestDTO;
import com.bank.management.dto.TransaccionResponseDTO;
import com.bank.management.service.TransaccionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TransaccionControllerTest {

    private MockMvc mockMvc;
    private TransaccionService transaccionService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        transaccionService = Mockito.mock(TransaccionService.class);
        TransaccionController controller = new TransaccionController(transaccionService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }
    //Caso exitoso: crear-transaccion

    @Test
    void crearTransaccion_success() throws Exception {

        TransaccionRequestDTO request = new TransaccionRequestDTO();
        request.setCuentaId(1L);
        request.setMonto(new BigDecimal("500.00"));
        request.setTipo("DEPOSITO");

        TransaccionResponseDTO response = new TransaccionResponseDTO();
        response.setId(1L);
        response.setCuentaId(1L);
        response.setMonto(new BigDecimal("500.00"));
        response.setTipo("DEPOSITO");

        Mockito.when(transaccionService.crear(Mockito.any(TransaccionRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/transacciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.monto").value(500.00))
                .andExpect(jsonPath("$.tipo").value("DEPOSITO"));

        Mockito.verify(transaccionService).crear(Mockito.any(TransaccionRequestDTO.class));
    }

    //Caso fallido  transaccion tipo invalida
    @Test
    void crearTransaccion_tipoInvalido_fail() throws Exception {

        TransaccionRequestDTO request = new TransaccionRequestDTO();
        request.setCuentaId(1L);
        request.setMonto(new BigDecimal("100.00"));
        request.setTipo("TRANSFERENCIA");

        Mockito.when(transaccionService.crear(Mockito.any(TransaccionRequestDTO.class)))
                .thenThrow(new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.BAD_REQUEST,
                        "Tipo inválido: use DEPOSITO o RETIRO"
                ));

        mockMvc.perform(post("/api/transacciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        Mockito.verify(transaccionService).crear(Mockito.any(TransaccionRequestDTO.class));
    }

}