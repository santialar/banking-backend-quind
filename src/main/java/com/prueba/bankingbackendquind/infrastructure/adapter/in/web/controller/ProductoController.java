package com.prueba.bankingbackendquind.infrastructure.adapter.in.web.controller;


import com.prueba.bankingbackendquind.domain.model.Producto;
import com.prueba.bankingbackendquind.domain.port.in.ProductoUseCase;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.EstadoCuentaRequestDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.ProductoRequestDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.ProductoResponseDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.mapper.ProductoWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "API para gestión de productos financieros")
public class ProductoController {

    private final ProductoUseCase productoUseCase;
    private final ProductoWebMapper productoWebMapper;

    public ProductoController(ProductoUseCase productoUseCase, ProductoWebMapper productoWebMapper) {
        this.productoUseCase = productoUseCase;
        this.productoWebMapper = productoWebMapper;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto financiero")
    public ResponseEntity<ProductoResponseDto> crearProducto(@Valid @RequestBody ProductoRequestDto productoRequestDto) {
        Producto producto = productoUseCase.crearProducto(
                productoRequestDto.getTipoCuenta(),
                productoRequestDto.getClienteId(),
                productoRequestDto.isExentaGMF()
        );
        ProductoResponseDto responseDto = productoWebMapper.toResponseDto(producto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Obtiene un producto específico por su ID")
    public ResponseEntity<ProductoResponseDto> obtenerProductoPorId(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        Producto producto = productoUseCase.obtenerProductoPorId(id);
        ProductoResponseDto responseDto = productoWebMapper.toResponseDto(producto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    @Operation(summary = "Obtener producto por número de cuenta",
            description = "Obtiene un producto específico por su número de cuenta")
    public ResponseEntity<ProductoResponseDto> obtenerProductoPorNumeroCuenta(
            @Parameter(description = "Número de cuenta del producto")
            @PathVariable String numeroCuenta) {
        Producto producto = productoUseCase.obtenerProductoPorNumeroCuenta(numeroCuenta);
        ProductoResponseDto responseDto = productoWebMapper.toResponseDto(producto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Obtener productos por cliente",
            description = "Obtiene todos los productos de un cliente específico")
    public ResponseEntity<List<ProductoResponseDto>> obtenerProductosPorCliente(
            @Parameter(description = "ID del cliente") @PathVariable Long clienteId) {
        List<Producto> productos = productoUseCase.obtenerProductosPorCliente(clienteId);
        List<ProductoResponseDto> responseDtos = productoWebMapper.toResponseDtoList(productos);
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Obtiene la lista de todos los productos")
    public ResponseEntity<List<ProductoResponseDto>> obtenerTodosLosProductos() {
        List<Producto> productos = productoUseCase.obtenerTodosLosProductos();
        List<ProductoResponseDto> responseDtos = productoWebMapper.toResponseDtoList(productos);
        return ResponseEntity.ok(responseDtos);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado del producto", description = "Cambia el estado de un producto financiero")
    public ResponseEntity<ProductoResponseDto> cambiarEstadoProducto(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Valid @RequestBody EstadoCuentaRequestDto estadoCuentaRequestDto) {
        Producto producto = productoUseCase.cambiarEstadoProducto(id, estadoCuentaRequestDto.getEstado());
        ProductoResponseDto responseDto = productoWebMapper.toResponseDto(producto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema")
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        productoUseCase.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
