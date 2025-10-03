package com.bank.management.service;

import com.bank.management.dto.MessageResponseDTO;
import com.bank.management.dto.UsuarioRequestDTO;
import com.bank.management.dto.UsuarioResponseDTO;
import com.bank.management.entity.Usuario;
import com.bank.management.mapper.UsuarioMapper;
import com.bank.management.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import com.bank.management.exception.UsuarioNotFoundException;
import com.bank.management.exception.DuplicatedDataException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public UsuarioResponseDTO guardar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicatedDataException("Usuario", dto.getEmail());

    }
        Usuario usuario = usuarioMapper.toEntity(dto);
        return usuarioMapper.toResponseDto(usuarioRepository.save(usuario));
    }

    @Override
    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResponseDto)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    @Override
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO dto) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
        usuarioMapper.updateEntityFromDto(dto, existente);
        Usuario actualizado = usuarioRepository.save(existente);

        return usuarioMapper.toResponseDto(actualizado);
    }


    @Override
    public MessageResponseDTO eliminar(Long id) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
        usuarioRepository.delete(existente);

        return new MessageResponseDTO("Usuario con id " + id + " eliminado exitosamente", id);
    }



}

