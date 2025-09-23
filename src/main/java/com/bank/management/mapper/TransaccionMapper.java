package com.bank.management.mapper;

import com.bank.management.dto.TransaccionRequestDTO;
import com.bank.management.dto.TransaccionResponseDTO;
import com.bank.management.entity.Transaccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransaccionMapper {

    @Mapping(source = "cuentaId", target = "cuenta.id")
    Transaccion toEntity(TransaccionRequestDTO dto);
    @Mapping(source = "cuenta.id", target = "cuentaId")
    TransaccionResponseDTO toDto(Transaccion transaccion);
    void updateEntity(TransaccionRequestDTO dto, @MappingTarget Transaccion transaccion);
}

