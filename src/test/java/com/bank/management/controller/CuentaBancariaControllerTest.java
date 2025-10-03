package com.bank.management.controller;

import com.bank.management.dto.CuentaRequestDTO;
import com.bank.management.dto.CuentaResponseDTO;
import com.bank.management.exception.CuentaNotFoundException;
import com.bank.management.service.CuentaBancariaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaBancariaController.class)
class CuentaBancariaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CuentaBancariaService cuentaBancariaService;

    // Caso exitoso
    @Test
    void crearCuenta_successful() throws Exception {
        CuentaRequestDTO request = new CuentaRequestDTO();
        request.setUsuarioId(1L);
        request.setTipoCuenta("AHORRO");
        request.setSaldoInicial(BigDecimal.valueOf(1000));


        CuentaResponseDTO response = new CuentaResponseDTO();
        response.setId(1L);
        response.setUsuarioId(1L);
        response.setSaldo(BigDecimal.valueOf(1000.0));
        response.setTipoCuenta("AHORRO");



        Mockito.when(cuentaBancariaService.guardarCuenta(any(CuentaRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipoCuenta").value("AHORRO"));
    }

    //  Caso fallido
    @Test
    void obtenerCuenta_notFound() throws Exception {
        Long cuentaId = 99L;

        Mockito.when(cuentaBancariaService.obtenerPorId(cuentaId))
                .thenThrow(new CuentaNotFoundException(cuentaId));

        mockMvc.perform(get("/api/cuentas/{id}", cuentaId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cuenta bancaria con id 99 no encontrada"))
                .andExpect(jsonPath("$.id").value(99));
    }
}
