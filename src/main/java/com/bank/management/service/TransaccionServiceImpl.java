package com.bank.management.service;

import com.bank.management.dto.TransaccionRequestDTO;
import com.bank.management.dto.TransaccionResponseDTO;
import com.bank.management.entity.Transaccion;
import com.bank.management.mapper.TransaccionMapper;
import com.bank.management.repository.TransaccionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final TransaccionMapper transaccionMapper;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository,
                                  TransaccionMapper transaccionMapper) {
        this.transaccionRepository = transaccionRepository;
        this.transaccionMapper = transaccionMapper;
    }

    @Override
    public TransaccionResponseDTO crear(TransaccionRequestDTO dto) {
        Transaccion entidad = transaccionMapper.toEntity(dto);
        entidad = transaccionRepository.save(entidad);
        return transaccionMapper.toDto(entidad);
    }

    @Override
    public List<TransaccionResponseDTO> obtenerTodos() {
        return transaccionRepository.findAll()
                .stream()
                .map(transaccionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransaccionResponseDTO obtenerPorId(Long id) {
        return transaccionRepository.findById(id)
                .map(transaccionMapper::toDto)
                .orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        transaccionRepository.deleteById(id);
    }

}
