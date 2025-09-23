package com.bank.management.controller;

import com.bank.management.dto.UsuarioRequestDTO;
import com.bank.management.dto.UsuarioResponseDTO;
import com.bank.management.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @Operation(summary = "Crear cuenta de usuario")
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.guardar(dto));
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por id")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.obtenerPorId(id);
        return (usuario != null) ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO actualizado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(actualizado);//añadi hoy
    }
}
