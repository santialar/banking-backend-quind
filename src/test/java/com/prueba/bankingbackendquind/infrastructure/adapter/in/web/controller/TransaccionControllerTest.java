package com.prueba.bankingbackendquind.infrastructure.adapter.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.bankingbackendquind.domain.exception.TransaccionException;
import com.prueba.bankingbackendquind.domain.model.Transaccion;
import com.prueba.bankingbackendquind.domain.model.enums.TipoTransaccion;
import com.prueba.bankingbackendquind.domain.port.in.TransaccionUseCase;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.TransaccionRequestDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto.TransaccionResponseDto;
import com.prueba.bankingbackendquind.infrastructure.adapter.in.web.mapper.TransaccionWebMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransaccionController.class)
class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransaccionUseCase transaccionUseCase;

    @MockitoBean
    private TransaccionWebMapper transaccionWebMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Transaccion transaccionValida;
    private TransaccionRequestDto transaccionRequestDto;
    private TransaccionResponseDto transaccionResponseDto;

    @BeforeEach
    void setUp() {
        transaccionValida = new Transaccion();
        transaccionValida.setId(1L);
        transaccionValida.setTipoTransaccion(TipoTransaccion.CONSIGNACION);
        transaccionValida.setMonto(new BigDecimal("500.00"));
        transaccionValida.setDescripcion("Consignación de prueba");
        transaccionValida.setNumeroCuentaOrigen("5312345678");
        transaccionValida.setFechaCreacion(LocalDateTime.now());

        transaccionRequestDto = new TransaccionRequestDto();
        transaccionRequestDto.setNumeroCuentaOrigen("5312345678");
        transaccionRequestDto.setMonto(new BigDecimal("500.00"));
        transaccionRequestDto.setDescripcion("Consignación de prueba");

        transaccionResponseDto = new TransaccionResponseDto();
        transaccionResponseDto.setId(1L);
        transaccionResponseDto.setTipoTransaccion(TipoTransaccion.CONSIGNACION);
        transaccionResponseDto.setMonto(new BigDecimal("500.00"));
        transaccionResponseDto.setDescripcion("Consignación de prueba");
        transaccionResponseDto.setNumeroCuentaOrigen("5312345678");
        transaccionResponseDto.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void realizarConsignacionDeberiaRetornar201CuandoConsignacionEsExitosa() throws Exception {
        when(transaccionUseCase.realizarConsignacion(anyString(), any(BigDecimal.class), anyString()))
                .thenReturn(transaccionValida);
        when(transaccionWebMapper.toResponseDto(any(Transaccion.class)))
                .thenReturn(transaccionResponseDto);

        mockMvc.perform(post("/api/transacciones/consignacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaccionRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipoTransaccion").value("CONSIGNACION"))
                .andExpect(jsonPath("$.monto").value(500.00))
                .andExpect(jsonPath("$.numeroCuentaOrigen").value("5312345678"));
    }

    @Test
    void realizarConsignacionDeberiaRetornar400CuandoMontoEsInvalido() throws Exception {
        transaccionRequestDto.setMonto(new BigDecimal("-100.00"));

        mockMvc.perform(post("/api/transacciones/consignacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaccionRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void realizarRetiroDeberiaRetornar201CuandoRetiroEsExitoso() throws Exception {
        transaccionValida.setTipoTransaccion(TipoTransaccion.RETIRO);
        transaccionResponseDto.setTipoTransaccion(TipoTransaccion.RETIRO);

        when(transaccionUseCase.realizarRetiro(anyString(), any(BigDecimal.class), anyString()))
                .thenReturn(transaccionValida);
        when(transaccionWebMapper.toResponseDto(any(Transaccion.class)))
                .thenReturn(transaccionResponseDto);

        mockMvc.perform(post("/api/transacciones/retiro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaccionRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoTransaccion").value("RETIRO"));
    }

    @Test
    void realizarRetiroDeberiaRetornar400CuandoSaldoEsInsuficiente() throws Exception {
        when(transaccionUseCase.realizarRetiro(anyString(), any(BigDecimal.class), anyString()))
                .thenThrow(new TransaccionException("Saldo insuficiente para realizar el retiro"));

        mockMvc.perform(post("/api/transacciones/retiro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaccionRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("TRANSACCION_ERROR"));
    }

    @Test
    void realizarTransferenciaDeberiaRetornar201CuandoTransferenciaEsExitosa() throws Exception {
        transaccionRequestDto.setNumeroCuentaDestino("3387654321");
        transaccionValida.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA);
        transaccionValida.setNumeroCuentaDestino("3387654321");
        transaccionResponseDto.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA);
        transaccionResponseDto.setNumeroCuentaDestino("3387654321");

        when(transaccionUseCase.realizarTransferencia(anyString(), anyString(), any(BigDecimal.class), anyString()))
                .thenReturn(transaccionValida);
        when(transaccionWebMapper.toResponseDto(any(Transaccion.class)))
                .thenReturn(transaccionResponseDto);

        mockMvc.perform(post("/api/transacciones/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaccionRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoTransaccion").value("TRANSFERENCIA"))
                .andExpect(jsonPath("$.numeroCuentaDestino").value("3387654321"));
    }

    @Test
    void realizarTransferenciaDeberiaRetornar400CuandoCuentaDestinoEsNula() throws Exception {

        mockMvc.perform(post("/api/transacciones/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaccionRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerTransaccionPorId_DeberiaRetornar200_CuandoTransaccionExiste() throws Exception {
        when(transaccionUseCase.obtenerTransaccionPorId(1L)).thenReturn(transaccionValida);
        when(transaccionWebMapper.toResponseDto(any(Transaccion.class))).thenReturn(transaccionResponseDto);

        mockMvc.perform(get("/api/transacciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void obtenerTransaccionesPorCuentaDeberiaRetornar200ConListaDeTransacciones() throws Exception {
        String numeroCuenta = "5312345678";
        List<Transaccion> transacciones = Arrays.asList(transaccionValida);
        List<TransaccionResponseDto> transaccionesDto = Arrays.asList(transaccionResponseDto);

        when(transaccionUseCase.obtenerTransaccionesPorCuenta(numeroCuenta)).thenReturn(transacciones);
        when(transaccionWebMapper.toResponseDtoList(any(List.class))).thenReturn(transaccionesDto);

        mockMvc.perform(get("/api/transacciones/cuenta/" + numeroCuenta))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void obtenerTodasLasTransaccionesDeberiaRetornar200ConListaCompleta() throws Exception {
        List<Transaccion> transacciones = Arrays.asList(transaccionValida);
        List<TransaccionResponseDto> transaccionesDto = Arrays.asList(transaccionResponseDto);

        when(transaccionUseCase.obtenerTodasLasTransacciones()).thenReturn(transacciones);
        when(transaccionWebMapper.toResponseDtoList(any(List.class))).thenReturn(transaccionesDto);

        mockMvc.perform(get("/api/transacciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
