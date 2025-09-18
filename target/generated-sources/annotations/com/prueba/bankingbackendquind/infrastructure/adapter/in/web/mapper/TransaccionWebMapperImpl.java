package com.prueba.bankingbackendquind.infrastructure.adapter.in.web.mapper;

import com.prueba.bankingbackendquind.domain.model.Transaccion;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.TransaccionResponseDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-17T23:04:10-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class TransaccionWebMapperImpl implements TransaccionWebMapper {

    @Override
    public TransaccionResponseDto toResponseDto(Transaccion domain) {
        if ( domain == null ) {
            return null;
        }

        TransaccionResponseDto transaccionResponseDto = new TransaccionResponseDto();

        transaccionResponseDto.setId( domain.getId() );
        transaccionResponseDto.setTipoTransaccion( domain.getTipoTransaccion() );
        transaccionResponseDto.setMonto( domain.getMonto() );
        transaccionResponseDto.setDescripcion( domain.getDescripcion() );
        transaccionResponseDto.setFechaCreacion( domain.getFechaCreacion() );
        transaccionResponseDto.setCuentaOrigenId( domain.getCuentaOrigenId() );
        transaccionResponseDto.setCuentaDestinoId( domain.getCuentaDestinoId() );
        transaccionResponseDto.setNumeroCuentaOrigen( domain.getNumeroCuentaOrigen() );
        transaccionResponseDto.setNumeroCuentaDestino( domain.getNumeroCuentaDestino() );

        return transaccionResponseDto;
    }

    @Override
    public List<TransaccionResponseDto> toResponseDtoList(List<Transaccion> domainList) {
        if ( domainList == null ) {
            return null;
        }

        List<TransaccionResponseDto> list = new ArrayList<TransaccionResponseDto>( domainList.size() );
        for ( Transaccion transaccion : domainList ) {
            list.add( toResponseDto( transaccion ) );
        }

        return list;
    }
}
