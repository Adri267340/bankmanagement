package com.bank.management.service;

import com.bank.management.dto.UsuarioRequestDTO;
import com.bank.management.dto.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDTO guardar(UsuarioRequestDTO dto);
    List<UsuarioResponseDTO> obtenerTodos();
    UsuarioResponseDTO obtenerPorId(Long id);
    void eliminar(Long id);
}
