package com.bank.management.controller;

import com.bank.management.dto.CuentaRequestDTO;
import com.bank.management.dto.CuentaResponseDTO;
import com.bank.management.service.CuentaBancariaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaBancariaController.class)
class CuentaBancariaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CuentaBancariaService cuentaBancariaService;

    // ✅ Caso exitoso: crear cuenta
    @Test
    void crearCuenta_successful() throws Exception {
        CuentaResponseDTO response = new CuentaResponseDTO();
        response.setId(1L);
        response.setSaldo(BigDecimal.valueOf(1000));
        response.setUsuarioId(10L);

        Mockito.when(cuentaBancariaService.guardar(Mockito.any(CuentaRequestDTO.class)))
                .thenReturn(response);

        String requestJson = """
                {
                    "saldo": 1000,
                    "usuarioId": 10
                }
                """;

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.saldo").value(1000))
                .andExpect(jsonPath("$.usuarioId").value(10L));
    }

    // ❌ Caso de fallo: obtener cuenta inexistente
    @Test
    void obtenerPorId_notFound() throws Exception {
        Mockito.when(cuentaBancariaService.obtenerPorId(99L)).thenReturn(null);

        mockMvc.perform(get("/api/cuentas/99"))
                .andExpect(status().isNotFound());
    }
}


