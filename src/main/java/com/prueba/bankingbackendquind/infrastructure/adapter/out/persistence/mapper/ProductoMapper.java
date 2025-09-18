package com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.mapper;

import com.prueba.bankingbackendquind.domain.model.Producto;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(target = "clienteId", source = "cliente.id")
    @Mapping(target = "transacciones", ignore = true)
    Producto toDomain(ProductoEntity entity);

    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "transaccionesOrigen", ignore = true)
    @Mapping(target = "transaccionesDestino", ignore = true)
    ProductoEntity toEntity(Producto domain);
}
