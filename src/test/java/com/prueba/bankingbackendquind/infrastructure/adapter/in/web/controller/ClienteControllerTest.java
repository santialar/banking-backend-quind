package com.prueba.bankingbackendquind.infrastructure.adapter.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.bankingbackendquind.domain.exception.BusinessException;
import com.prueba.bankingbackendquind.domain.exception.ClienteNotFoundException;
import com.prueba.bankingbackendquind.domain.model.Cliente;
import com.prueba.bankingbackendquind.domain.model.enums.TipoIdentificacion;
import com.prueba.bankingbackendquind.domain.port.in.ClienteUseCase;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.ClienteRequestDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.ClienteResponseDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.mapper.ClienteWebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteUseCase clienteUseCase;

    @MockitoBean
    private ClienteWebMapper clienteWebMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente clienteValido;
    private ClienteRequestDto clienteRequestDto;
    private ClienteResponseDto clienteResponseDto;

    @BeforeEach
    void setUp() {
        clienteValido = new Cliente();
        clienteValido.setId(1L);
        clienteValido.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
        clienteValido.setNumeroIdentificacion("12345678");
        clienteValido.setNombres("Juan Carlos");
        clienteValido.setApellido("Pérez");
        clienteValido.setCorreoElectronico("juan.perez@email.com");
        clienteValido.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        clienteValido.setFechaCreacion(LocalDateTime.now());
        clienteValido.setFechaModificacion(LocalDateTime.now());

        clienteRequestDto = new ClienteRequestDto();
        clienteRequestDto.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
        clienteRequestDto.setNumeroIdentificacion("12345678");
        clienteRequestDto.setNombres("Juan Carlos");
        clienteRequestDto.setApellido("Pérez");
        clienteRequestDto.setCorreoElectronico("juan.perez@email.com");
        clienteRequestDto.setFechaNacimiento(LocalDate.of(1990, 5, 15));

        clienteResponseDto = new ClienteResponseDto();
        clienteResponseDto.setId(1L);
        clienteResponseDto.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
        clienteResponseDto.setNumeroIdentificacion("12345678");
        clienteResponseDto.setNombres("Juan Carlos");
        clienteResponseDto.setApellido("Pérez");
        clienteResponseDto.setCorreoElectronico("juan.perez@email.com");
        clienteResponseDto.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        clienteResponseDto.setFechaCreacion(LocalDateTime.now());
        clienteResponseDto.setFechaModificacion(LocalDateTime.now());
    }

    @Test
    void crearClienteDeberiaRetornar201CuandoClienteEsValido() throws Exception {
        when(clienteWebMapper.toDomain(any(ClienteRequestDto.class))).thenReturn(clienteValido);
        when(clienteUseCase.crearCliente(any(Cliente.class))).thenReturn(clienteValido);
        when(clienteWebMapper.toResponseDto(any(Cliente.class))).thenReturn(clienteResponseDto);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombres").value("Juan Carlos"))
                .andExpect(jsonPath("$.numeroIdentificacion").value("12345678"));
    }

    @Test
    void crearClienteDeberiaRetornar400CuandoNombreEsInvalido() throws Exception {
        clienteRequestDto.setNombres("A");

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearCliente_DeberiaRetornar400_CuandoEmailEsInvalido() throws Exception {
        clienteRequestDto.setCorreoElectronico("email-invalido");

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerClientePorIdDeberiaRetornar200CuandoClienteExiste() throws Exception {
        when(clienteUseCase.obtenerClientePorId(1L)).thenReturn(clienteValido);
        when(clienteWebMapper.toResponseDto(any(Cliente.class))).thenReturn(clienteResponseDto);

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombres").value("Juan Carlos"));
    }

    @Test
    void obtenerClientePorIdDeberiaRetornar404CuandoClienteNoExiste() throws Exception {
        when(clienteUseCase.obtenerClientePorId(anyLong()))
                .thenThrow(new ClienteNotFoundException("Cliente no encontrado con ID: 1"));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("CLIENTE_NOT_FOUND"));
    }

    @Test
    void obtenerTodosLosClientesDeberiaRetornar200ConListaDeClientes() throws Exception {
        List<Cliente> clientes = Arrays.asList(clienteValido);
        List<ClienteResponseDto> clientesDto = Arrays.asList(clienteResponseDto);

        when(clienteUseCase.obtenerTodosLosClientes()).thenReturn(clientes);
        when(clienteWebMapper.toResponseDtoList(any(List.class))).thenReturn(clientesDto);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void actualizarClienteDeberiaRetornar200CuandoActualizacionEsExitosa() throws Exception {
        when(clienteWebMapper.toDomain(any(ClienteRequestDto.class))).thenReturn(clienteValido);
        when(clienteUseCase.actualizarCliente(anyLong(), any(Cliente.class))).thenReturn(clienteValido);
        when(clienteWebMapper.toResponseDto(any(Cliente.class))).thenReturn(clienteResponseDto);

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void eliminarClienteDeberiaRetornar204CuandoEliminacionEsExitosa() throws Exception {
        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarClienteDeberiaRetornar400CuandoClienteTieneProductos() throws Exception {
        doThrow(new BusinessException("No se puede eliminar el cliente porque tiene productos vinculados"))
                .when(clienteUseCase).eliminarCliente(anyLong());

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BUSINESS_ERROR"));
    }

    @Test
    void obtenerClientePorNumeroIdentificacionDeberiaRetornar200CuandoClienteExiste() throws Exception {
        String numeroIdentificacion = "12345678";
        when(clienteUseCase.obtenerClientePorNumeroIdentificacion(numeroIdentificacion)).thenReturn(clienteValido);
        when(clienteWebMapper.toResponseDto(any(Cliente.class))).thenReturn(clienteResponseDto);

        mockMvc.perform(get("/api/clientes/identificacion/" + numeroIdentificacion))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroIdentificacion").value(numeroIdentificacion));
    }
}
