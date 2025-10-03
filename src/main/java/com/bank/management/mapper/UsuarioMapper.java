package com.bank.management.mapper;

import com.bank.management.dto.UsuarioRequestDTO;
import com.bank.management.dto.UsuarioResponseDTO;
import com.bank.management.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioRequestDTO dto);
    UsuarioResponseDTO toResponseDto(Usuario usuario);
    void updateEntityFromDto(UsuarioRequestDTO dto, @MappingTarget Usuario entity);//añadi hoy
}

