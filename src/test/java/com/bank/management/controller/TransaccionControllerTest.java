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

    @Test
    void crearTransaccion_success() throws Exception {
        //1. Datos de entrada y salida
        TransaccionRequestDTO request = new TransaccionRequestDTO();
        request.setCuentaId(1L);
        request.setMonto(new BigDecimal("500.00"));
        request.setTipo("DEPOSITO");

        TransaccionResponseDTO response = new TransaccionResponseDTO();
        response.setId(1L);
        response.setCuentaId(1L);
        response.setMonto(new BigDecimal("500.00"));
        response.setTipo("DEPOSITO");

        //2. Mock
        Mockito.when(transaccionService.crear(Mockito.any(TransaccionRequestDTO.class)))
                .thenReturn(response);

        //3 y 4. Llamar y verificar
        mockMvc.perform(post("/api/transacciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.monto").value(500.00))
                .andExpect(jsonPath("$.tipo").value("DEPOSITO"));

        //5. Verificar interacción
        Mockito.verify(transaccionService).crear(Mockito.any(TransaccionRequestDTO.class));
    }
}