package com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.mapper;

import com.prueba.bankingbackendquind.domain.model.Cliente;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.ClienteEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-17T23:04:09-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class ClienteMapperImpl implements ClienteMapper {

    @Override
    public Cliente toDomain(ClienteEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Cliente cliente = new Cliente();

        cliente.setId( entity.getId() );
        cliente.setTipoIdentificacion( entity.getTipoIdentificacion() );
        cliente.setNumeroIdentificacion( entity.getNumeroIdentificacion() );
        cliente.setNombres( entity.getNombres() );
        cliente.setApellido( entity.getApellido() );
        cliente.setCorreoElectronico( entity.getCorreoElectronico() );
        cliente.setFechaNacimiento( entity.getFechaNacimiento() );
        cliente.setFechaCreacion( entity.getFechaCreacion() );
        cliente.setFechaModificacion( entity.getFechaModificacion() );

        return cliente;
    }

    @Override
    public ClienteEntity toEntity(Cliente domain) {
        if ( domain == null ) {
            return null;
        }

        ClienteEntity clienteEntity = new ClienteEntity();

        clienteEntity.setId( domain.getId() );
        clienteEntity.setTipoIdentificacion( domain.getTipoIdentificacion() );
        clienteEntity.setNumeroIdentificacion( domain.getNumeroIdentificacion() );
        clienteEntity.setNombres( domain.getNombres() );
        clienteEntity.setApellido( domain.getApellido() );
        clienteEntity.setCorreoElectronico( domain.getCorreoElectronico() );
        clienteEntity.setFechaNacimiento( domain.getFechaNacimiento() );
        clienteEntity.setFechaCreacion( domain.getFechaCreacion() );
        clienteEntity.setFechaModificacion( domain.getFechaModificacion() );

        return clienteEntity;
    }

    @Override
    public void updateEntity(Cliente domain, ClienteEntity entity) {
        if ( domain == null ) {
            return;
        }

        entity.setTipoIdentificacion( domain.getTipoIdentificacion() );
        entity.setNumeroIdentificacion( domain.getNumeroIdentificacion() );
        entity.setNombres( domain.getNombres() );
        entity.setApellido( domain.getApellido() );
        entity.setCorreoElectronico( domain.getCorreoElectronico() );
        entity.setFechaNacimiento( domain.getFechaNacimiento() );
        entity.setFechaModificacion( domain.getFechaModificacion() );
    }
}
