package com.prueba.bankingbackendquind.infrastructure.adapter.in.web.mapper;


import com.prueba.bankingbackendquind.domain.model.Cliente;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.ClienteRequestDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.ClienteResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteWebMapper {

    Cliente toDomain(ClienteRequestDto dto);

    ClienteResponseDto toResponseDto(Cliente domain);

    List<ClienteResponseDto> toResponseDtoList(List<Cliente> domainList);
}
