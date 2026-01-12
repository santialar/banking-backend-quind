package com.prueba.bankingbackendquind.infrastructure.adapter.in.web.mapper;

import com.prueba.bankingbackendquind.domain.model.Producto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.ProductoResponseDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-12T16:10:34-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.17 (Microsoft)"
)
@Component
public class ProductoWebMapperImpl implements ProductoWebMapper {

    @Override
    public ProductoResponseDto toResponseDto(Producto domain) {
        if ( domain == null ) {
            return null;
        }

        ProductoResponseDto productoResponseDto = new ProductoResponseDto();

        productoResponseDto.setId( domain.getId() );
        productoResponseDto.setTipoCuenta( domain.getTipoCuenta() );
        productoResponseDto.setNumeroCuenta( domain.getNumeroCuenta() );
        productoResponseDto.setEstado( domain.getEstado() );
        productoResponseDto.setSaldo( domain.getSaldo() );
        productoResponseDto.setExentaGMF( domain.isExentaGMF() );
        productoResponseDto.setFechaCreacion( domain.getFechaCreacion() );
        productoResponseDto.setFechaModificacion( domain.getFechaModificacion() );
        productoResponseDto.setClienteId( domain.getClienteId() );

        return productoResponseDto;
    }

    @Override
    public List<ProductoResponseDto> toResponseDtoList(List<Producto> domainList) {
        if ( domainList == null ) {
            return null;
        }

        List<ProductoResponseDto> list = new ArrayList<ProductoResponseDto>( domainList.size() );
        for ( Producto producto : domainList ) {
            list.add( toResponseDto( producto ) );
        }

        return list;
    }
}
