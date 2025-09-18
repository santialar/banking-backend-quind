package com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.mapper;

import com.prueba.bankingbackendquind.domain.model.Transaccion;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.TransaccionEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-17T23:04:09-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class TransaccionMapperImpl implements TransaccionMapper {

    @Override
    public Transaccion toDomain(TransaccionEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Transaccion transaccion = new Transaccion();

        transaccion.setCuentaOrigenId( entityCuentaOrigenId( entity ) );
        transaccion.setCuentaDestinoId( entityCuentaDestinoId( entity ) );
        transaccion.setId( entity.getId() );
        transaccion.setTipoTransaccion( entity.getTipoTransaccion() );
        transaccion.setMonto( entity.getMonto() );
        transaccion.setDescripcion( entity.getDescripcion() );
        transaccion.setFechaCreacion( entity.getFechaCreacion() );
        transaccion.setNumeroCuentaOrigen( entity.getNumeroCuentaOrigen() );
        transaccion.setNumeroCuentaDestino( entity.getNumeroCuentaDestino() );

        return transaccion;
    }

    @Override
    public TransaccionEntity toEntity(Transaccion domain) {
        if ( domain == null ) {
            return null;
        }

        TransaccionEntity transaccionEntity = new TransaccionEntity();

        transaccionEntity.setId( domain.getId() );
        transaccionEntity.setTipoTransaccion( domain.getTipoTransaccion() );
        transaccionEntity.setMonto( domain.getMonto() );
        transaccionEntity.setDescripcion( domain.getDescripcion() );
        transaccionEntity.setFechaCreacion( domain.getFechaCreacion() );
        transaccionEntity.setNumeroCuentaOrigen( domain.getNumeroCuentaOrigen() );
        transaccionEntity.setNumeroCuentaDestino( domain.getNumeroCuentaDestino() );

        return transaccionEntity;
    }

    private Long entityCuentaOrigenId(TransaccionEntity transaccionEntity) {
        if ( transaccionEntity == null ) {
            return null;
        }
        ProductoEntity cuentaOrigen = transaccionEntity.getCuentaOrigen();
        if ( cuentaOrigen == null ) {
            return null;
        }
        Long id = cuentaOrigen.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityCuentaDestinoId(TransaccionEntity transaccionEntity) {
        if ( transaccionEntity == null ) {
            return null;
        }
        ProductoEntity cuentaDestino = transaccionEntity.getCuentaDestino();
        if ( cuentaDestino == null ) {
            return null;
        }
        Long id = cuentaDestino.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
