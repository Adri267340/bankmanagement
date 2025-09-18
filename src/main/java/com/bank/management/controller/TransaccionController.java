package com.bank.management.controller;

import com.bank.management.dto.TransaccionRequestDTO;
import com.bank.management.dto.TransaccionResponseDTO;
import com.bank.management.service.TransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping
    public ResponseEntity<TransaccionResponseDTO> crearTransaccion(@RequestBody TransaccionRequestDTO dto) {
        return ResponseEntity.ok(transaccionService.crear(dto));
    }

    @GetMapping
    @Operation(summary = "Obtener todas las transacciones")
    public ResponseEntity<List<TransaccionResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(transaccionService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> obtenerPorId(@PathVariable Long id) {
        TransaccionResponseDTO transaccion = transaccionService.obtenerPorId(id);
        return (transaccion != null) ? ResponseEntity.ok(transaccion) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTransaccion(@PathVariable Long id) {
        transaccionService.eliminar(id); // hay que agregar este método en el service
        return ResponseEntity.noContent().build();
    }
}

