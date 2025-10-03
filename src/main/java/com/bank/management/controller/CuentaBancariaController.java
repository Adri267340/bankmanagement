package com.bank.management.controller;

import com.bank.management.dto.CuentaRequestDTO;
import com.bank.management.dto.CuentaResponseDTO;
import com.bank.management.service.CuentaBancariaService;
import com.bank.management.service.CuentaBancariaServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import jakarta.validation.Valid;
import com.bank.management.dto.RetiroRequestDTO;
import com.bank.management.dto.MessageResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaBancariaController {


    private final CuentaBancariaService cuentaBancariaService;

    public CuentaBancariaController(CuentaBancariaService cuentaBancariaService) {
        this.cuentaBancariaService = cuentaBancariaService;
    }

    @PostMapping
    @Operation(summary = "Crear cuenta Bancaria")
    public ResponseEntity<CuentaResponseDTO> crear(@RequestBody @Valid CuentaRequestDTO dto) {
        CuentaResponseDTO cuenta = cuentaBancariaService.guardarCuenta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cuenta);
    }



    @GetMapping
    @Operation(summary = "Obtener todas las cuentas Bancarias")
    public ResponseEntity<List<CuentaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(cuentaBancariaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cuenta por numero de id")
    public ResponseEntity<CuentaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaBancariaService.obtenerPorId(id));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cuenta")

    public ResponseEntity<MessageResponseDTO> eliminarCuenta(@PathVariable Long id) {
        cuentaBancariaService.eliminar(id);
        return ResponseEntity.ok(new MessageResponseDTO("Cuenta eliminada con éxito", id));
    }



    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cuenta bancaria")
    public ResponseEntity<CuentaResponseDTO> actualizarCuenta(
            @PathVariable Long id,
            @RequestBody CuentaRequestDTO dto) {
        CuentaResponseDTO actualizado = cuentaBancariaService.actualizarCuenta(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @PostMapping("/{id}/retirar")
    public ResponseEntity<String> retirar(@PathVariable Long id, @RequestParam BigDecimal monto) {
        cuentaBancariaService.retirar(id, monto);
        return ResponseEntity.ok("Retiro exitoso de " + monto);
    }

    @PostMapping("/retirar")
    public ResponseEntity<String> retirar(
            @Valid @RequestBody RetiroRequestDTO request
    ) {
        // Aquí llamas al service con los datos del DTO
        cuentaBancariaService.retirar(request.getCuentaId(), request.getMonto());

        return ResponseEntity.ok("Retiro realizado con éxito");
    }

}



