package com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.mapper;


import com.prueba.bankingbackendquind.domain.model.Cliente;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.ClienteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "productos", ignore = true)
    Cliente toDomain(ClienteEntity entity);

    @Mapping(target = "productos", ignore = true)
    ClienteEntity toEntity(Cliente domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productos", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    void updateEntity(Cliente domain, @MappingTarget ClienteEntity entity);
}
