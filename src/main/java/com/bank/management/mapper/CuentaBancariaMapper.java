package com.bank.management.mapper;

import com.bank.management.dto.CuentaRequestDTO;
import com.bank.management.dto.CuentaResponseDTO;
import com.bank.management.entity.CuentaBancaria;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CuentaBancariaMapper {

    CuentaBancaria toEntity(CuentaRequestDTO dto);

    CuentaResponseDTO toDto(CuentaBancaria entity);
}
