package com.prueba.bankingbackendquind.infrastructure.adapter.in.web.mapper;

import com.prueba.bankingbackendquind.domain.model.Cliente;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.ClienteRequestDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.ClienteResponseDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-17T23:04:09-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class ClienteWebMapperImpl implements ClienteWebMapper {

    @Override
    public Cliente toDomain(ClienteRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Cliente cliente = new Cliente();

        cliente.setTipoIdentificacion( dto.getTipoIdentificacion() );
        cliente.setNumeroIdentificacion( dto.getNumeroIdentificacion() );
        cliente.setNombres( dto.getNombres() );
        cliente.setApellido( dto.getApellido() );
        cliente.setCorreoElectronico( dto.getCorreoElectronico() );
        cliente.setFechaNacimiento( dto.getFechaNacimiento() );

        return cliente;
    }

    @Override
    public ClienteResponseDto toResponseDto(Cliente domain) {
        if ( domain == null ) {
            return null;
        }

        ClienteResponseDto clienteResponseDto = new ClienteResponseDto();

        clienteResponseDto.setId( domain.getId() );
        clienteResponseDto.setTipoIdentificacion( domain.getTipoIdentificacion() );
        clienteResponseDto.setNumeroIdentificacion( domain.getNumeroIdentificacion() );
        clienteResponseDto.setNombres( domain.getNombres() );
        clienteResponseDto.setApellido( domain.getApellido() );
        clienteResponseDto.setCorreoElectronico( domain.getCorreoElectronico() );
        clienteResponseDto.setFechaNacimiento( domain.getFechaNacimiento() );
        clienteResponseDto.setFechaCreacion( domain.getFechaCreacion() );
        clienteResponseDto.setFechaModificacion( domain.getFechaModificacion() );

        return clienteResponseDto;
    }

    @Override
    public List<ClienteResponseDto> toResponseDtoList(List<Cliente> domainList) {
        if ( domainList == null ) {
            return null;
        }

        List<ClienteResponseDto> list = new ArrayList<ClienteResponseDto>( domainList.size() );
        for ( Cliente cliente : domainList ) {
            list.add( toResponseDto( cliente ) );
        }

        return list;
    }
}
