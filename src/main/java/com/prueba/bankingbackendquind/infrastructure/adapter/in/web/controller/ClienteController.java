package com.prueba.bankingbackendquind.infrastructure.adapter.in.web.controller;


import com.prueba.bankingbackendquind.domain.model.Cliente;
import com.prueba.bankingbackendquind.domain.port.in.ClienteUseCase;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.ClienteRequestDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.ClienteResponseDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.mapper.ClienteWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API para gestión de clientes")
public class ClienteController {

    private final ClienteUseCase clienteUseCase;
    private final ClienteWebMapper clienteWebMapper;

    public ClienteController(ClienteUseCase clienteUseCase, ClienteWebMapper clienteWebMapper) {
        this.clienteUseCase = clienteUseCase;
        this.clienteWebMapper = clienteWebMapper;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente", description = "Crea un nuevo cliente en el sistema")
    public ResponseEntity<ClienteResponseDto> crearCliente(@Valid @RequestBody ClienteRequestDto clienteRequestDto) {
        Cliente cliente = clienteWebMapper.toDomain(clienteRequestDto);
        Cliente clienteCreado = clienteUseCase.crearCliente(cliente);
        ClienteResponseDto responseDto = clienteWebMapper.toResponseDto(clienteCreado);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID", description = "Obtiene un cliente específico por su ID")
    public ResponseEntity<ClienteResponseDto> obtenerClientePorId(
            @Parameter(description = "ID del cliente") @PathVariable Long id) {
        Cliente cliente = clienteUseCase.obtenerClientePorId(id);
        ClienteResponseDto responseDto = clienteWebMapper.toResponseDto(cliente);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/identificacion/{numeroIdentificacion}")
    @Operation(summary = "Obtener cliente por número de identificación",
               description = "Obtiene un cliente específico por su número de identificación")
    public ResponseEntity<ClienteResponseDto> obtenerClientePorNumeroIdentificacion(
            @Parameter(description = "Número de identificación del cliente")
            @PathVariable String numeroIdentificacion) {
        Cliente cliente = clienteUseCase.obtenerClientePorNumeroIdentificacion(numeroIdentificacion);
        ClienteResponseDto responseDto = clienteWebMapper.toResponseDto(cliente);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los clientes", description = "Obtiene la lista de todos los clientes")
    public ResponseEntity<List<ClienteResponseDto>> obtenerTodosLosClientes() {
        List<Cliente> clientes = clienteUseCase.obtenerTodosLosClientes();
        List<ClienteResponseDto> responseDtos = clienteWebMapper.toResponseDtoList(clientes);
        return ResponseEntity.ok(responseDtos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente", description = "Actualiza la información de un cliente existente")
    public ResponseEntity<ClienteResponseDto> actualizarCliente(
            @Parameter(description = "ID del cliente") @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDto clienteRequestDto) {
        Cliente cliente = clienteWebMapper.toDomain(clienteRequestDto);
        Cliente clienteActualizado = clienteUseCase.actualizarCliente(id, cliente);
        ClienteResponseDto responseDto = clienteWebMapper.toResponseDto(clienteActualizado);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente del sistema")
    public ResponseEntity<Void> eliminarCliente(
            @Parameter(description = "ID del cliente") @PathVariable Long id) {
        clienteUseCase.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
