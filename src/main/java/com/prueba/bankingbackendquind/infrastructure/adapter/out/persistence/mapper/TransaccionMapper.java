package com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.mapper;

import com.prueba.bankingbackendquind.domain.model.Transaccion;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.TransaccionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransaccionMapper {

    @Mapping(target = "cuentaOrigenId", source = "cuentaOrigen.id")
    @Mapping(target = "cuentaDestinoId", source = "cuentaDestino.id")
    Transaccion toDomain(TransaccionEntity entity);

    @Mapping(target = "cuentaOrigen", ignore = true)
    @Mapping(target = "cuentaDestino", ignore = true)
    TransaccionEntity toEntity(Transaccion domain);
}
