package com.prueba.bankingbackendquind.application.service;

import com.prueba.bankingbackendquind.domain.exception.ProductoNotFoundException;
import com.prueba.bankingbackendquind.domain.exception.TransaccionException;
import com.prueba.bankingbackendquind.domain.model.Producto;
import com.prueba.bankingbackendquind.domain.model.Transaccion;
import com.prueba.bankingbackendquind.domain.model.enums.EstadoCuenta;
import com.prueba.bankingbackendquind.domain.model.enums.TipoCuenta;
import com.prueba.bankingbackendquind.domain.model.enums.TipoTransaccion;
import com.prueba.bankingbackendquind.domain.port.out.ProductoRepositoryPort;
import com.prueba.bankingbackendquind.domain.port.out.TransaccionRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceTest {

    @Mock
    private TransaccionRepositoryPort transaccionRepository;

    @Mock
    private ProductoRepositoryPort productoRepository;

    @InjectMocks
    private TransaccionService transaccionService;

    private Producto cuentaAhorros;
    private Producto cuentaCorriente;
    private Transaccion transaccionValida;

    @BeforeEach
    void setUp() {
        cuentaAhorros = new Producto();
        cuentaAhorros.setId(1L);
        cuentaAhorros.setTipoCuenta(TipoCuenta.CUENTA_AHORROS);
        cuentaAhorros.setNumeroCuenta("5312345678");
        cuentaAhorros.setEstado(EstadoCuenta.ACTIVA);
        cuentaAhorros.setSaldo(new BigDecimal("1000.00"));

        cuentaCorriente = new Producto();
        cuentaCorriente.setId(2L);
        cuentaCorriente.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuentaCorriente.setNumeroCuenta("3387654321");
        cuentaCorriente.setEstado(EstadoCuenta.ACTIVA);
        cuentaCorriente.setSaldo(new BigDecimal("2000.00"));

        transaccionValida = new Transaccion();
        transaccionValida.setId(1L);
        transaccionValida.setTipoTransaccion(TipoTransaccion.CONSIGNACION);
        transaccionValida.setMonto(new BigDecimal("500.00"));
        transaccionValida.setDescripcion("Consignación de prueba");
    }

    @Test
    void realizarConsignacionDeberiaConsignarExitosamente() {
        String numeroCuenta = "5312345678";
        BigDecimal monto = new BigDecimal("500.00");
        String descripcion = "Consignación de prueba";

        when(productoRepository.findByNumeroCuenta(numeroCuenta)).thenReturn(Optional.of(cuentaAhorros));
        when(productoRepository.save(any(Producto.class))).thenReturn(cuentaAhorros);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccionValida);

        Transaccion resultado = transaccionService.realizarConsignacion(numeroCuenta, monto, descripcion);

        assertNotNull(resultado);
        assertEquals(TipoTransaccion.CONSIGNACION, resultado.getTipoTransaccion());
        assertEquals(monto, resultado.getMonto());
        verify(productoRepository).save(any(Producto.class));
        verify(transaccionRepository).save(any(Transaccion.class));
    }

    @Test
    void realizarConsignacionDeberiaLanzarExcepcionSiCuentaNoExiste() {
        String numeroCuenta = "1234567890";
        BigDecimal monto = new BigDecimal("500.00");
        when(productoRepository.findByNumeroCuenta(numeroCuenta)).thenReturn(Optional.empty());

        ProductoNotFoundException exception = assertThrows(ProductoNotFoundException.class,
                () -> transaccionService.realizarConsignacion(numeroCuenta, monto, "Descripción"));

        assertEquals("Cuenta no encontrada: " + numeroCuenta, exception.getMessage());
    }

    @Test
    void realizarConsignacionDeberiaLanzarExcepcionSiCuentaInactiva() {
        cuentaAhorros.setEstado(EstadoCuenta.INACTIVA);
        when(productoRepository.findByNumeroCuenta(anyString())).thenReturn(Optional.of(cuentaAhorros));

        TransaccionException exception = assertThrows(TransaccionException.class,
                () -> transaccionService.realizarConsignacion("5312345678", new BigDecimal("500.00"), "Descripción"));

        assertEquals("La cuenta debe estar activa para realizar transacciones", exception.getMessage());
    }

    @Test
    void realizarRetiroDeberiaRetirarExitosamente() {
        String numeroCuenta = "5312345678";
        BigDecimal monto = new BigDecimal("500.00");
        String descripcion = "Retiro de prueba";

        transaccionValida.setTipoTransaccion(TipoTransaccion.RETIRO);
        when(productoRepository.findByNumeroCuenta(numeroCuenta)).thenReturn(Optional.of(cuentaAhorros));
        when(productoRepository.save(any(Producto.class))).thenReturn(cuentaAhorros);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccionValida);

        Transaccion resultado = transaccionService.realizarRetiro(numeroCuenta, monto, descripcion);

        assertNotNull(resultado);
        assertEquals(TipoTransaccion.RETIRO, resultado.getTipoTransaccion());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void realizarRetiroDeberiaLanzarExcepcionSiSaldoInsuficiente() {
        cuentaAhorros.setSaldo(new BigDecimal("100.00"));
        BigDecimal monto = new BigDecimal("500.00");
        when(productoRepository.findByNumeroCuenta(anyString())).thenReturn(Optional.of(cuentaAhorros));

        TransaccionException exception = assertThrows(TransaccionException.class,
                () -> transaccionService.realizarRetiro("5312345678", monto, "Descripción"));

        assertEquals("Saldo insuficiente para realizar el retiro", exception.getMessage());
    }

    @Test
    void realizarTransferenciaDeberiaTransferirExitosamente() {
        String cuentaOrigen = "5312345678";
        String cuentaDestino = "3387654321";
        BigDecimal monto = new BigDecimal("500.00");
        String descripcion = "Transferencia de prueba";

        transaccionValida.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA);
        when(productoRepository.findByNumeroCuenta(cuentaOrigen)).thenReturn(Optional.of(cuentaAhorros));
        when(productoRepository.findByNumeroCuenta(cuentaDestino)).thenReturn(Optional.of(cuentaCorriente));
        when(productoRepository.save(any(Producto.class))).thenReturn(cuentaAhorros).thenReturn(cuentaCorriente);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccionValida);

        Transaccion resultado = transaccionService.realizarTransferencia(cuentaOrigen, cuentaDestino, monto, descripcion);

        assertNotNull(resultado);
        assertEquals(TipoTransaccion.TRANSFERENCIA, resultado.getTipoTransaccion());
        verify(productoRepository, times(2)).save(any(Producto.class));
    }

    @Test
    void realizarTransferenciaDeberiaLanzarExcepcionSiCuentasIguales() {
        String numeroCuenta = "5312345678";
        BigDecimal monto = new BigDecimal("500.00");

        TransaccionException exception = assertThrows(TransaccionException.class,
                () -> transaccionService.realizarTransferencia(numeroCuenta, numeroCuenta, monto, "Descripción"));

        assertEquals("No se puede transferir a la misma cuenta", exception.getMessage());
    }

    @Test
    void realizarTransferenciaDeberiaLanzarExcepcionSiSaldoInsuficiente() {
        cuentaAhorros.setSaldo(new BigDecimal("100.00"));
        BigDecimal monto = new BigDecimal("500.00");

        when(productoRepository.findByNumeroCuenta("5312345678")).thenReturn(Optional.of(cuentaAhorros));
        when(productoRepository.findByNumeroCuenta("3387654321")).thenReturn(Optional.of(cuentaCorriente));

        TransaccionException exception = assertThrows(TransaccionException.class,
                () -> transaccionService.realizarTransferencia("5312345678", "3387654321", monto, "Descripción"));

        assertEquals("Saldo insuficiente en la cuenta origen para realizar la transferencia",
                exception.getMessage());
    }

    @Test
    void obtenerTransaccionesPorCuentaDeberiaRetornarTransacciones() {
        String numeroCuenta = "5312345678";

        Transaccion t1 = new Transaccion();
        t1.setFechaCreacion(java.time.LocalDateTime.now().minusHours(1));

        Transaccion t2 = new Transaccion();
        t2.setFechaCreacion(java.time.LocalDateTime.now());

        when(transaccionRepository.findByNumeroCuentaOrigen(numeroCuenta))
                .thenReturn(java.util.Arrays.asList(t1));
        when(transaccionRepository.findByNumeroCuentaDestino(numeroCuenta))
                .thenReturn(java.util.Arrays.asList(t2));

        List<Transaccion> resultado = transaccionService.obtenerTransaccionesPorCuenta(numeroCuenta);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertSame(t2, resultado.get(0));
        assertSame(t1, resultado.get(1));
    }

    @Test
    void obtenerTodasLasTransaccionesDeberiaRetornarListado() {
        Transaccion t1 = new Transaccion();
        Transaccion t2 = new Transaccion();
        when(transaccionRepository.findAll()).thenReturn(java.util.Arrays.asList(t1, t2));

        List<Transaccion> resultado = transaccionService.obtenerTodasLasTransacciones();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(transaccionRepository).findAll();
    }

    @Test
    void obtenerTransaccionPorIdDeberiaRetornarCuandoExiste() {
        Transaccion t = new Transaccion();
        t.setId(99L);
        when(transaccionRepository.findById(99L)).thenReturn(java.util.Optional.of(t));

        Transaccion resultado = transaccionService.obtenerTransaccionPorId(99L);

        assertNotNull(resultado);
        assertEquals(99L, resultado.getId());
        verify(transaccionRepository).findById(99L);
    }

    @Test
    void obtenerTransaccionPorIdDeberiaLanzarExcepcionCuandoNoExiste() {
        when(transaccionRepository.findById(123L)).thenReturn(java.util.Optional.empty());

        TransaccionException ex = assertThrows(
                TransaccionException.class,
                () -> transaccionService.obtenerTransaccionPorId(123L)
        );

        assertEquals("Transacción no encontrada con ID: 123", ex.getMessage());
        verify(transaccionRepository).findById(123L);
    }

    @Test
    void realizarConsignacionDeberiaLanzarExcepcionSiMontoEsNull() {
        TransaccionException ex = assertThrows(
                TransaccionException.class,
                () -> transaccionService.realizarConsignacion("5312345678", null, "Desc")
        );
        assertEquals("El monto debe ser mayor a cero", ex.getMessage());
        verifyNoInteractions(productoRepository, transaccionRepository);
    }

    @Test
    void realizarConsignacionDeberiaLanzarExcepcionSiMontoEsCero() {
        BigDecimal monto = BigDecimal.ZERO;

        TransaccionException exception = assertThrows(TransaccionException.class,
                () -> transaccionService.realizarConsignacion("5312345678", monto, "Descripción"));

        assertEquals("El monto debe ser mayor a cero", exception.getMessage());
    }

    @Test
    void realizarConsignacionDeberiaLanzarExcepcionSiMontoEsNegativo() {
        BigDecimal monto = new BigDecimal("-100.00");

        TransaccionException exception = assertThrows(TransaccionException.class,
                () -> transaccionService.realizarConsignacion("5312345678", monto, "Descripción"));

        assertEquals("El monto debe ser mayor a cero", exception.getMessage());
    }
}
