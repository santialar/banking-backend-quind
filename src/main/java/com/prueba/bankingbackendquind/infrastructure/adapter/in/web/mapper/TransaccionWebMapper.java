package com.prueba.bankingbackendquind.infrastructure.adapter.in.web.mapper;


import com.prueba.bankingbackendquind.domain.model.Transaccion;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.TransaccionResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransaccionWebMapper {

    TransaccionResponseDto toResponseDto(Transaccion domain);

    List<TransaccionResponseDto> toResponseDtoList(List<Transaccion> domainList);
}
