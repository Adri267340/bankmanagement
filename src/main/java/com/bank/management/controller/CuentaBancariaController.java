package com.bank.management.controller;

import com.bank.management.dto.CuentaRequestDTO;
import com.bank.management.dto.CuentaResponseDTO;
import com.bank.management.service.CuentaBancariaService;
import com.bank.management.service.CuentaBancariaServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaBancariaController {

    private final CuentaBancariaService cuentaBancariaService;

    public CuentaBancariaController(CuentaBancariaService cuentaBancariaService) {
        this.cuentaBancariaService = cuentaBancariaService;
    }

    @PostMapping
    @Operation(summary = "Crear cuenta")
    public ResponseEntity<CuentaResponseDTO> crearCuenta(@RequestBody CuentaRequestDTO dto) {
        return ResponseEntity.ok(cuentaBancariaService.guardar(dto));
    }

    @GetMapping
    @Operation(summary = "Obtener todas las cuentas Bancarias")
    public ResponseEntity<List<CuentaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(cuentaBancariaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cuenta por numero de id")
    public ResponseEntity<CuentaResponseDTO> obtenerPorId(@PathVariable Long id) {
        CuentaResponseDTO cuenta = cuentaBancariaService.obtenerPorId(id);
        return (cuenta != null) ? ResponseEntity.ok(cuenta) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cuenta")
    public ResponseEntity<Void> eliminarCuenta(@PathVariable Long id) {
        cuentaBancariaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
