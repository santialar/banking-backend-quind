package com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence;

import com.prueba.bankingbackendquind.domain.model.Producto;
import com.prueba.bankingbackendquind.domain.model.enums.EstadoCuenta;
import com.prueba.bankingbackendquind.domain.model.enums.TipoCuenta;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.ClienteEntity;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.mapper.ProductoMapper;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.repository.ClienteJpaRepository;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.repository.ProductoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoRepositoryAdapterTest {

    @Mock
    private ProductoJpaRepository productoJpaRepository;

    @Mock
    private ClienteJpaRepository clienteJpaRepository;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoRepositoryAdapter productoRepositoryAdapter;

    private Producto productoDomain;
    private ProductoEntity productoEntity;
    private ProductoEntity productoEntitySaved;
    private ClienteEntity clienteEntity;

    @BeforeEach
    void setUp() {
        clienteEntity = new ClienteEntity();
        clienteEntity.setId(1L);
        clienteEntity.setNombres("Juan Carlos");
        clienteEntity.setApellido("Pérez");
        clienteEntity.setNumeroIdentificacion("1234567890");

        productoDomain = new Producto();
        productoDomain.setId(1L);
        productoDomain.setTipoCuenta(TipoCuenta.CUENTA_AHORROS);
        productoDomain.setNumeroCuenta("5312345678");
        productoDomain.setEstado(EstadoCuenta.ACTIVA);
        productoDomain.setSaldo(new BigDecimal("1000.00"));
        productoDomain.setExentaGMF(false);
        productoDomain.setClienteId(1L);
        productoDomain.setFechaCreacion(LocalDateTime.now());
        productoDomain.setFechaModificacion(LocalDateTime.now());

        productoEntity = new ProductoEntity();
        productoEntity.setTipoCuenta(TipoCuenta.CUENTA_AHORROS);
        productoEntity.setNumeroCuenta("5312345678");
        productoEntity.setEstado(EstadoCuenta.ACTIVA);
        productoEntity.setSaldo(new BigDecimal("1000.00"));
        productoEntity.setExentaGMF(false);

        productoEntitySaved = new ProductoEntity();
        productoEntitySaved.setId(1L);
        productoEntitySaved.setTipoCuenta(TipoCuenta.CUENTA_AHORROS);
        productoEntitySaved.setNumeroCuenta("5312345678");
        productoEntitySaved.setEstado(EstadoCuenta.ACTIVA);
        productoEntitySaved.setSaldo(new BigDecimal("1000.00"));
        productoEntitySaved.setExentaGMF(false);
        productoEntitySaved.setCliente(clienteEntity);
        productoEntitySaved.setFechaCreacion(LocalDateTime.now());
        productoEntitySaved.setFechaModificacion(LocalDateTime.now());
    }

    @Test
    void save_DeberiaGuardarProductoConClienteIdValido() {
        when(productoMapper.toEntity(productoDomain)).thenReturn(productoEntity);
        when(clienteJpaRepository.findById(1L)).thenReturn(Optional.of(clienteEntity));
        when(productoJpaRepository.save(any(ProductoEntity.class))).thenReturn(productoEntitySaved);
        when(productoMapper.toDomain(productoEntitySaved)).thenReturn(productoDomain);

        Producto resultado = productoRepositoryAdapter.save(productoDomain);

        assertNotNull(resultado);
        assertEquals(productoDomain.getId(), resultado.getId());
        assertEquals(productoDomain.getNumeroCuenta(), resultado.getNumeroCuenta());
        assertEquals(productoDomain.getClienteId(), resultado.getClienteId());

        verify(productoMapper).toEntity(productoDomain);
        verify(clienteJpaRepository).findById(1L);
        verify(productoJpaRepository).save(any(ProductoEntity.class));
        verify(productoMapper).toDomain(productoEntitySaved);
    }

    @Test
    void save_DeberiaGuardarProductoSinClienteId() {
        productoDomain.setClienteId(null);
        when(productoMapper.toEntity(productoDomain)).thenReturn(productoEntity);
        when(productoJpaRepository.save(any(ProductoEntity.class))).thenReturn(productoEntitySaved);
        when(productoMapper.toDomain(productoEntitySaved)).thenReturn(productoDomain);

        Producto resultado = productoRepositoryAdapter.save(productoDomain);

        assertNotNull(resultado);
        assertEquals(productoDomain.getId(), resultado.getId());

        verify(clienteJpaRepository, never()).findById(any());
        verify(productoMapper).toEntity(productoDomain);
        verify(productoJpaRepository).save(any(ProductoEntity.class));
        verify(productoMapper).toDomain(productoEntitySaved);
    }

    @Test
    void save_DeberiaLanzarExcepcionCuandoClienteNoExiste() {
        Long clienteIdInexistente = 999L;
        productoDomain.setClienteId(clienteIdInexistente);
        when(productoMapper.toEntity(productoDomain)).thenReturn(productoEntity);
        when(clienteJpaRepository.findById(clienteIdInexistente)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoRepositoryAdapter.save(productoDomain);
        });

        assertEquals("Cliente no encontrado", exception.getMessage());
        verify(productoMapper).toEntity(productoDomain);
        verify(clienteJpaRepository).findById(clienteIdInexistente);
        verify(productoJpaRepository, never()).save(any());
    }

    @Test
    void findById_DeberiaRetornarProductoCuandoExiste() {
        Long id = 1L;
        when(productoJpaRepository.findById(id)).thenReturn(Optional.of(productoEntitySaved));
        when(productoMapper.toDomain(productoEntitySaved)).thenReturn(productoDomain);

        Optional<Producto> resultado = productoRepositoryAdapter.findById(id);

        assertTrue(resultado.isPresent());
        assertEquals(productoDomain.getId(), resultado.get().getId());
        assertEquals(productoDomain.getNumeroCuenta(), resultado.get().getNumeroCuenta());
        verify(productoJpaRepository).findById(id);
        verify(productoMapper).toDomain(productoEntitySaved);
    }

    @Test
    void findById_DeberiaRetornarEmptyCuandoNoExiste() {
        Long id = 999L;
        when(productoJpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoRepositoryAdapter.findById(id);

        assertTrue(resultado.isEmpty());
        verify(productoJpaRepository).findById(id);
        verify(productoMapper, never()).toDomain(any());
    }

    @Test
    void findByNumeroCuenta_DeberiaRetornarProductoCuandoExiste() {
        String numeroCuenta = "5312345678";
        when(productoJpaRepository.findByNumeroCuenta(numeroCuenta))
                .thenReturn(Optional.of(productoEntitySaved));
        when(productoMapper.toDomain(productoEntitySaved)).thenReturn(productoDomain);

        Optional<Producto> resultado = productoRepositoryAdapter.findByNumeroCuenta(numeroCuenta);

        assertTrue(resultado.isPresent());
        assertEquals(productoDomain.getNumeroCuenta(), resultado.get().getNumeroCuenta());
        verify(productoJpaRepository).findByNumeroCuenta(numeroCuenta);
        verify(productoMapper).toDomain(productoEntitySaved);
    }

    @Test
    void findByNumeroCuenta_DeberiaRetornarEmptyCuandoNoExiste() {
        String numeroCuenta = "9999999999";
        when(productoJpaRepository.findByNumeroCuenta(numeroCuenta))
                .thenReturn(Optional.empty());

        Optional<Producto> resultado = productoRepositoryAdapter.findByNumeroCuenta(numeroCuenta);

        assertTrue(resultado.isEmpty());
        verify(productoJpaRepository).findByNumeroCuenta(numeroCuenta);
        verify(productoMapper, never()).toDomain(any());
    }

    @Test
    void findByClienteId_DeberiaRetornarListaDeProductos() {
        Long clienteId = 1L;
        ProductoEntity entity2 = new ProductoEntity();
        entity2.setId(2L);
        entity2.setNumeroCuenta("3387654321");
        entity2.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        entity2.setEstado(EstadoCuenta.ACTIVA);
        entity2.setSaldo(new BigDecimal("2000.00"));

        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNumeroCuenta("3387654321");
        producto2.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        producto2.setEstado(EstadoCuenta.ACTIVA);
        producto2.setSaldo(new BigDecimal("2000.00"));
        producto2.setClienteId(clienteId);

        List<ProductoEntity> entities = Arrays.asList(productoEntitySaved, entity2);
        when(productoJpaRepository.findByClienteId(clienteId)).thenReturn(entities);
        when(productoMapper.toDomain(productoEntitySaved)).thenReturn(productoDomain);
        when(productoMapper.toDomain(entity2)).thenReturn(producto2);

        List<Producto> resultado = productoRepositoryAdapter.findByClienteId(clienteId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(productoDomain.getId(), resultado.get(0).getId());
        assertEquals(producto2.getId(), resultado.get(1).getId());
        verify(productoJpaRepository).findByClienteId(clienteId);
        verify(productoMapper, times(2)).toDomain(any(ProductoEntity.class));
    }

    @Test
    void findByClienteId_DeberiaRetornarListaVaciaCuandoNoHayProductos() {
        Long clienteId = 999L;
        when(productoJpaRepository.findByClienteId(clienteId)).thenReturn(Arrays.asList());

        List<Producto> resultado = productoRepositoryAdapter.findByClienteId(clienteId);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(productoJpaRepository).findByClienteId(clienteId);
        verify(productoMapper, never()).toDomain(any());
    }

    @Test
    void findAll_DeberiaRetornarListaDeProductos() {
        ProductoEntity entity2 = new ProductoEntity();
        entity2.setId(2L);
        entity2.setNumeroCuenta("3387654321");
        entity2.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNumeroCuenta("3387654321");
        producto2.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        List<ProductoEntity> entities = Arrays.asList(productoEntitySaved, entity2);
        when(productoJpaRepository.findAll()).thenReturn(entities);
        when(productoMapper.toDomain(productoEntitySaved)).thenReturn(productoDomain);
        when(productoMapper.toDomain(entity2)).thenReturn(producto2);

        List<Producto> resultado = productoRepositoryAdapter.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(productoDomain.getId(), resultado.get(0).getId());
        assertEquals(producto2.getId(), resultado.get(1).getId());
        verify(productoJpaRepository).findAll();
        verify(productoMapper, times(2)).toDomain(any(ProductoEntity.class));
    }

    @Test
    void findAll_DeberiaRetornarListaVaciaCuandoNoHayProductos() {
        when(productoJpaRepository.findAll()).thenReturn(Arrays.asList());

        List<Producto> resultado = productoRepositoryAdapter.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(productoJpaRepository).findAll();
        verify(productoMapper, never()).toDomain(any());
    }

    @Test
    void deleteById_DeberiaEliminarProducto() {
        Long id = 1L;
        doNothing().when(productoJpaRepository).deleteById(id);

        productoRepositoryAdapter.deleteById(id);

        verify(productoJpaRepository).deleteById(id);
    }

    @Test
    void existsByNumeroCuenta_DeberiaRetornarTrueCuandoExiste() {
        String numeroCuenta = "5312345678";
        when(productoJpaRepository.existsByNumeroCuenta(numeroCuenta)).thenReturn(true);

        boolean resultado = productoRepositoryAdapter.existsByNumeroCuenta(numeroCuenta);

        assertTrue(resultado);
        verify(productoJpaRepository).existsByNumeroCuenta(numeroCuenta);
    }

    @Test
    void existsByNumeroCuenta_DeberiaRetornarFalseCuandoNoExiste() {
        String numeroCuenta = "9999999999";
        when(productoJpaRepository.existsByNumeroCuenta(numeroCuenta)).thenReturn(false);

        boolean resultado = productoRepositoryAdapter.existsByNumeroCuenta(numeroCuenta);

        assertFalse(resultado);
        verify(productoJpaRepository).existsByNumeroCuenta(numeroCuenta);
    }

    @Test
    void countByClienteId_DeberiaRetornarCantidadDeProductos() {
        Long clienteId = 1L;
        int cantidadEsperada = 3;
        when(productoJpaRepository.countByClienteId(clienteId)).thenReturn(cantidadEsperada);

        int resultado = productoRepositoryAdapter.countByClienteId(clienteId);

        assertEquals(cantidadEsperada, resultado);
        verify(productoJpaRepository).countByClienteId(clienteId);
    }

    @Test
    void countByClienteId_DeberiaRetornarCeroCuandoNoHayProductos() {
        Long clienteId = 999L;
        when(productoJpaRepository.countByClienteId(clienteId)).thenReturn(0);

        int resultado = productoRepositoryAdapter.countByClienteId(clienteId);

        assertEquals(0, resultado);
        verify(productoJpaRepository).countByClienteId(clienteId);
    }
}