package com.prueba.bankingbackendquind.infrastructure.adapter.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.bankingbackendquind.domain.model.Producto;
import com.prueba.bankingbackendquind.domain.model.enums.EstadoCuenta;
import com.prueba.bankingbackendquind.domain.model.enums.TipoCuenta;
import com.prueba.bankingbackendquind.domain.port.in.ProductoUseCase;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.ProductoRequestDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.ProductoResponseDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.mapper.ProductoWebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoUseCase productoUseCase;

    @MockitoBean
    private ProductoWebMapper productoWebMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto productoValido;
    private ProductoRequestDto productoRequestDto;
    private ProductoResponseDto productoResponseDto;


    @BeforeEach
    void setUp() {
        productoValido = new Producto();
        productoValido.setId(1L);
        productoValido.setTipoCuenta(TipoCuenta.CUENTA_AHORROS);
        productoValido.setNumeroCuenta("5312345678");
        productoValido.setEstado(EstadoCuenta.ACTIVA);
        productoValido.setSaldo(BigDecimal.ZERO);
        productoValido.setExentaGMF(false);
        productoValido.setFechaCreacion(LocalDateTime.now());
        productoValido.setFechaModificacion(LocalDateTime.now());
        productoValido.setClienteId(10L);

        productoRequestDto = new ProductoRequestDto();
        productoRequestDto.setTipoCuenta(TipoCuenta.CUENTA_AHORROS);
        productoRequestDto.setClienteId(10L);
        productoRequestDto.setExentaGMF(false);

        productoResponseDto = new ProductoResponseDto();
        productoResponseDto.setId(1L);
        productoResponseDto.setTipoCuenta(TipoCuenta.CUENTA_AHORROS);
        productoResponseDto.setNumeroCuenta("5312345678");
        productoResponseDto.setEstado(EstadoCuenta.ACTIVA);
        productoResponseDto.setSaldo(BigDecimal.ZERO);
        productoResponseDto.setExentaGMF(false);
        productoResponseDto.setFechaCreacion(LocalDateTime.now());
        productoResponseDto.setFechaModificacion(LocalDateTime.now());
        productoResponseDto.setClienteId(10L);
    }

    @Test
    void crearProductoDeberiaRetornar201() throws Exception {
        when(productoUseCase.crearProducto(any(TipoCuenta.class), anyLong(), anyBoolean()))
                .thenReturn(productoValido);
        when(productoWebMapper.toResponseDto(any(Producto.class)))
                .thenReturn(productoResponseDto);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipoCuenta").value("CUENTA_AHORROS"))
                .andExpect(jsonPath("$.numeroCuenta").value("5312345678"))
                .andExpect(jsonPath("$.clienteId").value(10));
    }

    @Test
    void crearProductoDeberiaRetornar400SiFaltanCamposObligatorios() throws Exception {
        ProductoRequestDto invalido = new ProductoRequestDto();
        invalido.setExentaGMF(true);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerProductoPorIdDeberiaRetornar200() throws Exception {
        when(productoUseCase.obtenerProductoPorId(1L)).thenReturn(productoValido);
        when(productoWebMapper.toResponseDto(any(Producto.class))).thenReturn(productoResponseDto);

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void obtenerProductoPorNumeroCuentaDeberiaRetornar200() throws Exception {
        when(productoUseCase.obtenerProductoPorNumeroCuenta("5312345678")).thenReturn(productoValido);
        when(productoWebMapper.toResponseDto(any(Producto.class))).thenReturn(productoResponseDto);

        mockMvc.perform(get("/api/productos/cuenta/5312345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCuenta").value("5312345678"));
    }

    @Test
    void obtenerProductosPorClienteDeberiaRetornar200ConLista() throws Exception {
        List<Producto> productos = Arrays.asList(productoValido);
        List<ProductoResponseDto> productosDto = Arrays.asList(productoResponseDto);

        when(productoUseCase.obtenerProductosPorCliente(10L)).thenReturn(productos);
        when(productoWebMapper.toResponseDtoList(anyList())).thenReturn(productosDto);

        mockMvc.perform(get("/api/productos/cliente/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void obtenerTodosLosProductosDeberiaRetornar200ConLista() throws Exception {
        List<Producto> productos = Arrays.asList(productoValido);
        List<ProductoResponseDto> productosDto = Arrays.asList(productoResponseDto);

        when(productoUseCase.obtenerTodosLosProductos()).thenReturn(productos);
        when(productoWebMapper.toResponseDtoList(anyList())).thenReturn(productosDto);

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroCuenta").value("5312345678"));
    }


    @Test
    void cambiarEstadoProductoDeberiaRetornar200() throws Exception {
        var estadoReq = new com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.EstadoCuentaRequestDto();
        estadoReq.setEstado(EstadoCuenta.INACTIVA);

        Producto actualizado = new Producto();
        actualizado.setId(1L);
        actualizado.setTipoCuenta(TipoCuenta.CUENTA_AHORROS);
        actualizado.setNumeroCuenta("5312345678");
        actualizado.setEstado(EstadoCuenta.INACTIVA);
        actualizado.setSaldo(BigDecimal.ZERO);
        actualizado.setClienteId(10L);

        ProductoResponseDto actualizadoDto = new ProductoResponseDto();
        actualizadoDto.setId(1L);
        actualizadoDto.setTipoCuenta(TipoCuenta.CUENTA_AHORROS);
        actualizadoDto.setNumeroCuenta("5312345678");
        actualizadoDto.setEstado(EstadoCuenta.INACTIVA);
        actualizadoDto.setSaldo(BigDecimal.ZERO);
        actualizadoDto.setClienteId(10L);

        when(productoUseCase.cambiarEstadoProducto(eq(1L), eq(EstadoCuenta.INACTIVA))).thenReturn(actualizado);
        when(productoWebMapper.toResponseDto(any(Producto.class))).thenReturn(actualizadoDto);

        mockMvc.perform(patch("/api/productos/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadoReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("INACTIVA"));
    }

    @Test
    void cambiarEstadoProductoDeberiaRetornar400SiEstadoNull() throws Exception {
        var estadoReq = new com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.EstadoCuentaRequestDto();
        estadoReq.setEstado(null);

        mockMvc.perform(patch("/api/productos/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estadoReq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminarProductoDeberiaRetornar204() throws Exception {
        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }
}
