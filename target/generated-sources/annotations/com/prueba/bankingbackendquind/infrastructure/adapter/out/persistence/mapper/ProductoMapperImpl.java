package com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.mapper;

import com.prueba.bankingbackendquind.domain.model.Producto;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.ClienteEntity;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-17T23:04:09-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class ProductoMapperImpl implements ProductoMapper {

    @Override
    public Producto toDomain(ProductoEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Producto producto = new Producto();

        producto.setClienteId( entityClienteId( entity ) );
        producto.setId( entity.getId() );
        producto.setTipoCuenta( entity.getTipoCuenta() );
        producto.setNumeroCuenta( entity.getNumeroCuenta() );
        producto.setEstado( entity.getEstado() );
        producto.setSaldo( entity.getSaldo() );
        producto.setExentaGMF( entity.isExentaGMF() );
        producto.setFechaCreacion( entity.getFechaCreacion() );
        producto.setFechaModificacion( entity.getFechaModificacion() );

        return producto;
    }

    @Override
    public ProductoEntity toEntity(Producto domain) {
        if ( domain == null ) {
            return null;
        }

        ProductoEntity productoEntity = new ProductoEntity();

        productoEntity.setId( domain.getId() );
        productoEntity.setTipoCuenta( domain.getTipoCuenta() );
        productoEntity.setNumeroCuenta( domain.getNumeroCuenta() );
        productoEntity.setEstado( domain.getEstado() );
        productoEntity.setSaldo( domain.getSaldo() );
        productoEntity.setExentaGMF( domain.isExentaGMF() );
        productoEntity.setFechaCreacion( domain.getFechaCreacion() );
        productoEntity.setFechaModificacion( domain.getFechaModificacion() );

        return productoEntity;
    }

    private Long entityClienteId(ProductoEntity productoEntity) {
        if ( productoEntity == null ) {
            return null;
        }
        ClienteEntity cliente = productoEntity.getCliente();
        if ( cliente == null ) {
            return null;
        }
        Long id = cliente.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
