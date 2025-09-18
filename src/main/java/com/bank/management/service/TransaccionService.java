package com.bank.management.service;

import com.bank.management.dto.TransaccionRequestDTO;
import com.bank.management.dto.TransaccionResponseDTO;

import java.util.List;

public interface TransaccionService {
    TransaccionResponseDTO crear(TransaccionRequestDTO dto);
    List<TransaccionResponseDTO> obtenerTodos();
    List<TransaccionResponseDTO> obtenerPorId(Long id);

}
