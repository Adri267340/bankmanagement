package com.bank.management.mapper;

import com.bank.management.dto.CuentaRequestDTO;
import com.bank.management.dto.CuentaResponseDTO;
import com.bank.management.entity.CuentaBancaria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.lang.annotation.Target;


@Mapper(componentModel = "spring")
public interface CuentaBancariaMapper {

    @Mapping(source = "usuario.id", target = "usuarioId")
    CuentaResponseDTO toDto(CuentaBancaria cuenta);

    @Mapping(source = "usuarioId", target = "usuario.id")
    CuentaBancaria toEntity(CuentaRequestDTO dto);

    @Mapping(target="id",ignore = true)
    void updateEntity(CuentaRequestDTO dto, @MappingTarget CuentaBancaria cuenta);
}


