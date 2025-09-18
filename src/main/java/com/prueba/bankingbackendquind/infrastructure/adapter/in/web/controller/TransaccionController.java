package com.prueba.bankingbackendquind.infrastructure.adapter.in.web.controller;


import com.prueba.bankingbackendquind.domain.model.Transaccion;
import com.prueba.bankingbackendquind.domain.port.in.TransaccionUseCase;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.TransaccionRequestDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.TransaccionResponseDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.mapper.TransaccionWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
@Tag(name = "Transacciones", description = "API para gestión de transacciones financieras")
public class TransaccionController {

    private final TransaccionUseCase transaccionUseCase;
    private final TransaccionWebMapper transaccionWebMapper;

    public TransaccionController(TransaccionUseCase transaccionUseCase,
                                 TransaccionWebMapper transaccionWebMapper) {
        this.transaccionUseCase = transaccionUseCase;
        this.transaccionWebMapper = transaccionWebMapper;
    }

    @PostMapping("/consignacion")
    @Operation(summary = "Realizar consignación", description = "Realiza una consignación a una cuenta")
    public ResponseEntity<TransaccionResponseDto> realizarConsignacion(
            @Valid @RequestBody TransaccionRequestDto transaccionRequestDto) {
        Transaccion transaccion = transaccionUseCase.realizarConsignacion(
                transaccionRequestDto.getNumeroCuentaOrigen(),
                transaccionRequestDto.getMonto(),
                transaccionRequestDto.getDescripcion()
        );
        TransaccionResponseDto responseDto = transaccionWebMapper.toResponseDto(transaccion);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/retiro")
    @Operation(summary = "Realizar retiro", description = "Realiza un retiro de una cuenta")
    public ResponseEntity<TransaccionResponseDto> realizarRetiro(
            @Valid @RequestBody TransaccionRequestDto transaccionRequestDto) {
        Transaccion transaccion = transaccionUseCase.realizarRetiro(
                transaccionRequestDto.getNumeroCuentaOrigen(),
                transaccionRequestDto.getMonto(),
                transaccionRequestDto.getDescripcion()
        );
        TransaccionResponseDto responseDto = transaccionWebMapper.toResponseDto(transaccion);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/transferencia")
    @Operation(summary = "Realizar transferencia", description = "Realiza una transferencia entre cuentas")
    public ResponseEntity<TransaccionResponseDto> realizarTransferencia(
            @Valid @RequestBody TransaccionRequestDto transaccionRequestDto) {
        if (transaccionRequestDto.getNumeroCuentaDestino() == null ||
                transaccionRequestDto.getNumeroCuentaDestino().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de cuenta destino es obligatorio para transferencias");
        }

        Transaccion transaccion = transaccionUseCase.realizarTransferencia(
                transaccionRequestDto.getNumeroCuentaOrigen(),
                transaccionRequestDto.getNumeroCuentaDestino(),
                transaccionRequestDto.getMonto(),
                transaccionRequestDto.getDescripcion()
        );
        TransaccionResponseDto responseDto = transaccionWebMapper.toResponseDto(transaccion);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener transacción por ID", description = "Obtiene una transacción específica por su ID")
    public ResponseEntity<TransaccionResponseDto> obtenerTransaccionPorId(
            @Parameter(description = "ID de la transacción") @PathVariable Long id) {
        Transaccion transaccion = transaccionUseCase.obtenerTransaccionPorId(id);
        TransaccionResponseDto responseDto = transaccionWebMapper.toResponseDto(transaccion);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    @Operation(summary = "Obtener transacciones por cuenta",
            description = "Obtiene todas las transacciones de una cuenta específica")
    public ResponseEntity<List<TransaccionResponseDto>> obtenerTransaccionesPorCuenta(
            @Parameter(description = "Número de cuenta") @PathVariable String numeroCuenta) {
        List<Transaccion> transacciones = transaccionUseCase.obtenerTransaccionesPorCuenta(numeroCuenta);
        List<TransaccionResponseDto> responseDtos = transaccionWebMapper.toResponseDtoList(transacciones);
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las transacciones", description = "Obtiene la lista de todas las transacciones")
    public ResponseEntity<List<TransaccionResponseDto>> obtenerTodasLasTransacciones() {
        List<Transaccion> transacciones = transaccionUseCase.obtenerTodasLasTransacciones();
        List<TransaccionResponseDto> responseDtos = transaccionWebMapper.toResponseDtoList(transacciones);
        return ResponseEntity.ok(responseDtos);
    }
}
