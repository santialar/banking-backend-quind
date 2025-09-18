package com.prueba.bankingbackendquind.infrastructure.adapter.in.web.mapper;


import com.prueba.bankingbackendquind.domain.model.Producto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.ProductoResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoWebMapper {

    ProductoResponseDto toResponseDto(Producto domain);

    List<ProductoResponseDto> toResponseDtoList(List<Producto> domainList);
}
