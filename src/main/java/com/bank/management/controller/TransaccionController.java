package com.bank.management.controller;

import com.bank.management.dto.TransaccionRequestDTO;
import com.bank.management.dto.TransaccionResponseDTO;
import com.bank.management.service.TransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bank.management.dto.MessageResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @PostMapping
    @Operation(summary = "Crear transaccion")
    public ResponseEntity<TransaccionResponseDTO> crearTransaccion(@RequestBody TransaccionRequestDTO dto) {
        return ResponseEntity.ok(transaccionService.crear(dto));
    }

    @GetMapping
    @Operation(summary = "Obtener todas las transacciones")
    public ResponseEntity<List<TransaccionResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(transaccionService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener transaccion por numero de id")
    public ResponseEntity<TransaccionResponseDTO> obtenerPorId(@PathVariable Long id) {
        TransaccionResponseDTO transaccion = transaccionService.obtenerPorId(id);
        return (transaccion != null) ? ResponseEntity.ok(transaccion) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar transacción")
    public ResponseEntity<MessageResponseDTO> eliminarTransaccion(@PathVariable Long id) {
        transaccionService.eliminar(id);

        MessageResponseDTO response = new MessageResponseDTO(
                "Transacción eliminada con éxito", id
        );

        return ResponseEntity.ok(response);
    }




    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tarnsaccion")
    public ResponseEntity<TransaccionResponseDTO> actualizarTransaccion(
            @PathVariable Long id,
            @RequestBody TransaccionRequestDTO dto) {
        TransaccionResponseDTO actualizado = transaccionService.actualizarTransaccion(id, dto);
        return ResponseEntity.ok(actualizado);

    }
}

