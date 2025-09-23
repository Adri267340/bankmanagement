package com.bank.management.service;

import com.bank.management.dto.UsuarioRequestDTO;
import com.bank.management.dto.UsuarioResponseDTO;
import com.bank.management.entity.Usuario;
import com.bank.management.mapper.UsuarioMapper;
import com.bank.management.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

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
        Usuario usuario = usuarioMapper.toEntity(dto);
        return usuarioMapper.toDto(usuarioRepository.save(usuario));
    }

    @Override
    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDto)
                .orElse(null);
    }

    @Override
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO dto) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuarioMapper.updateEntityFromDto(dto, existente);
        Usuario actualizado = usuarioRepository.save(existente);

        return usuarioMapper.toDto(actualizado);
    } //añadi hoy
    @Override
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

}
