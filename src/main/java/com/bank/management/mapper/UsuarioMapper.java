package com.bank.management.mapper;

import com.bank.management.dto.UsuarioRequestDTO;
import com.bank.management.dto.UsuarioResponseDTO;
import com.bank.management.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    // De DTO a entidad
    Usuario toEntity(UsuarioRequestDTO dto);

    // De entidad a DTO
    UsuarioResponseDTO toDto(Usuario entity);
}

