package com.prueba.bankingbackendquind.application.service;

import com.prueba.bankingbackendquind.domain.exception.BusinessException;
import com.prueba.bankingbackendquind.domain.exception.ClienteNotFoundException;
import com.prueba.bankingbackendquind.domain.exception.ProductoNotFoundException;
import com.prueba.bankingbackendquind.domain.model.Cliente;
import com.prueba.bankingbackendquind.domain.model.Producto;
import com.prueba.bankingbackendquind.domain.model.enums.EstadoCuenta;
import com.prueba.bankingbackendquind.domain.model.enums.TipoCuenta;
import com.prueba.bankingbackendquind.domain.port.out.ClienteRepositoryPort;
import com.prueba.bankingbackendquind.domain.port.out.ProductoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepositoryPort productoRepository;

    @Mock
    private ClienteRepositoryPort clienteRepository;

    @InjectMocks
    private ProductoService productoService;

    private Cliente clienteValido;
    private Producto productoValido;

    @BeforeEach
    void setUp() {
        clienteValido = new Cliente();
        clienteValido.setId(1L);
        clienteValido.setNombres("Juan Carlos");

        productoValido = new Producto();
        productoValido.setId(1L);
        productoValido.setTipoCuenta(TipoCuenta.CUENTA_AHORROS);
        productoValido.setNumeroCuenta("5312345678");
        productoValido.setEstado(EstadoCuenta.ACTIVA);
        productoValido.setSaldo(BigDecimal.ZERO);
        productoValido.setClienteId(1L);
        productoValido.setExentaGMF(false);
    }

    @Test
    void crearProductoDeberiaCrearProductoExitosamente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteValido));
        when(productoRepository.existsByNumeroCuenta(anyString())).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoValido);

        Producto resultado = productoService.crearProducto(TipoCuenta.CUENTA_AHORROS, 1L, false);

        assertNotNull(resultado);
        assertEquals(TipoCuenta.CUENTA_AHORROS, resultado.getTipoCuenta());
        assertEquals(EstadoCuenta.ACTIVA, resultado.getEstado());
        assertEquals(BigDecimal.ZERO, resultado.getSaldo());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void crearProductoDeberiaLanzarExcepcionSiClienteNoExiste() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        ClienteNotFoundException exception = assertThrows(ClienteNotFoundException.class,
                () -> productoService.crearProducto(TipoCuenta.CUENTA_AHORROS, 1L, false));

        assertEquals("Cliente no encontrado con ID: 1", exception.getMessage());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void crearProductoDeberiaGenerarNuevoNumeroCuentaSiYaExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteValido));
        when(productoRepository.existsByNumeroCuenta(anyString()))
                .thenReturn(true)
                .thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoValido);

        Producto resultado = productoService.crearProducto(TipoCuenta.CUENTA_AHORROS, 1L, false);

        assertNotNull(resultado);
        verify(productoRepository, times(2)).existsByNumeroCuenta(anyString());
    }

    @Test
    void obtenerProductoPorIdDeberiaRetornarProductoExistente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoValido));

        Producto resultado = productoService.obtenerProductoPorId(1L);

        assertNotNull(resultado);
        assertEquals(productoValido.getId(), resultado.getId());
    }

    @Test
    void obtenerProductoPorIdDeberiaLanzarExcepcionSiProductoNoExiste() {
        when(productoRepository.findById(anyLong())).thenReturn(Optional.empty());

        ProductoNotFoundException exception = assertThrows(ProductoNotFoundException.class,
                () -> productoService.obtenerProductoPorId(1L));

        assertEquals("Producto no encontrado con ID: 1", exception.getMessage());
    }

    @Test
    void obtenerProductoPorNumeroCuentaDeberiaRetornarProductoExistente() {
        String numeroCuenta = "5312345678";
        when(productoRepository.findByNumeroCuenta(numeroCuenta)).thenReturn(Optional.of(productoValido));

        Producto resultado = productoService.obtenerProductoPorNumeroCuenta(numeroCuenta);

        assertNotNull(resultado);
        assertEquals(numeroCuenta, resultado.getNumeroCuenta());
    }

    @Test
    void obtenerProductosPorClienteDeberiaRetornarListaDeProductos() {
        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        List<Producto> productos = Arrays.asList(productoValido, producto2);

        when(productoRepository.findByClienteId(1L)).thenReturn(productos);

        List<Producto> resultado = productoService.obtenerProductosPorCliente(1L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    void obtenerTodosLosProductosDeberiaRetornarListado() {
        Producto p1 = new Producto();
        Producto p2 = new Producto();
        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> resultado = productoService.obtenerTodosLosProductos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(productoRepository).findAll();
    }


    @Test
    void cambiarEstadoProductoDeberiaActivarProducto() {
        productoValido.setEstado(EstadoCuenta.INACTIVA);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoValido));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoValido);

        Producto resultado = productoService.cambiarEstadoProducto(1L, EstadoCuenta.ACTIVA);

        assertNotNull(resultado);
        assertEquals(EstadoCuenta.ACTIVA, resultado.getEstado());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void cambiarEstadoProductoDeberiaLanzarExcepcionAlCancelarConSaldo() {
        productoValido.setSaldo(new BigDecimal("1000.00"));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoValido));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> productoService.cambiarEstadoProducto(1L, EstadoCuenta.CANCELADA));

        assertEquals("No se puede cancelar una cuenta con saldo diferente a cero",
                exception.getMessage());
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void cambiarEstadoProductoDeberiaInactivarProducto() {
        productoValido.setEstado(EstadoCuenta.ACTIVA);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoValido));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        Producto res = productoService.cambiarEstadoProducto(1L, EstadoCuenta.INACTIVA);

        assertEquals(EstadoCuenta.INACTIVA, res.getEstado());
        verify(productoRepository).save(productoValido);
    }

    @Test
    void cambiarEstadoProductoDeberiaCancelarProductoCuandoSaldoCero() {
        productoValido.setSaldo(BigDecimal.ZERO);
        productoValido.setEstado(EstadoCuenta.ACTIVA);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoValido));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        Producto res = productoService.cambiarEstadoProducto(1L, EstadoCuenta.CANCELADA);

        assertEquals(EstadoCuenta.CANCELADA, res.getEstado());
        verify(productoRepository).save(productoValido);
    }

    @Test
    void cambiarEstadoProductoConEstadoNullLanzaNPE() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoValido));
        assertThrows(NullPointerException.class,
                () -> productoService.cambiarEstadoProducto(1L, null));
    }

    @Test
    void eliminarProductoDeberiaEliminarProductoConSaldoCero() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoValido));

        productoService.eliminarProducto(1L);

        verify(productoRepository).deleteById(1L);
    }

    @Test
    void eliminarProductoDeberiaLanzarExcepcionSiTieneSaldo() {
        productoValido.setSaldo(new BigDecimal("500.00"));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoValido));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> productoService.eliminarProducto(1L));

        assertEquals("No se puede eliminar un producto con saldo diferente a cero",
                exception.getMessage());
        verify(productoRepository, never()).deleteById(anyLong());
    }
}
