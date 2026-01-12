package com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence;

import com.prueba.bankingbackendquind.domain.model.Transaccion;
import com.prueba.bankingbackendquind.domain.model.enums.TipoTransaccion;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.TransaccionEntity;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.mapper.TransaccionMapper;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.repository.ProductoJpaRepository;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.repository.TransaccionJpaRepository;
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
class TransaccionRepositoryAdapterTest {

    @Mock
    private TransaccionJpaRepository transaccionJpaRepository;

    @Mock
    private ProductoJpaRepository productoJpaRepository;

    @Mock
    private TransaccionMapper transaccionMapper;

    @InjectMocks
    private TransaccionRepositoryAdapter transaccionRepositoryAdapter;

    private Transaccion transaccionDomain;
    private TransaccionEntity transaccionEntity;
    private TransaccionEntity transaccionEntitySaved;
    private ProductoEntity cuentaOrigen;
    private ProductoEntity cuentaDestino;

    @BeforeEach
    void setUp() {
        cuentaOrigen = new ProductoEntity();
        cuentaOrigen.setId(1L);
        cuentaOrigen.setNumeroCuenta("5312345678");
        cuentaOrigen.setTipoCuenta(com.prueba.bankingbackendquind.domain.model.enums.TipoCuenta.CUENTA_AHORROS);

        cuentaDestino = new ProductoEntity();
        cuentaDestino.setId(2L);
        cuentaDestino.setNumeroCuenta("3387654321");
        cuentaDestino.setTipoCuenta(com.prueba.bankingbackendquind.domain.model.enums.TipoCuenta.CUENTA_CORRIENTE);

        transaccionDomain = new Transaccion();
        transaccionDomain.setId(1L);
        transaccionDomain.setTipoTransaccion(TipoTransaccion.CONSIGNACION);
        transaccionDomain.setMonto(new BigDecimal("500.00"));
        transaccionDomain.setDescripcion("Consignación de prueba");
        transaccionDomain.setCuentaOrigenId(1L);
        transaccionDomain.setCuentaDestinoId(2L);
        transaccionDomain.setNumeroCuentaOrigen("5312345678");
        transaccionDomain.setNumeroCuentaDestino("3387654321");
        transaccionDomain.setFechaCreacion(LocalDateTime.now());

        transaccionEntity = new TransaccionEntity();
        transaccionEntity.setTipoTransaccion(TipoTransaccion.CONSIGNACION);
        transaccionEntity.setMonto(new BigDecimal("500.00"));
        transaccionEntity.setDescripcion("Consignación de prueba");
        transaccionEntity.setNumeroCuentaOrigen("5312345678");
        transaccionEntity.setNumeroCuentaDestino("3387654321");

        transaccionEntitySaved = new TransaccionEntity();
        transaccionEntitySaved.setId(1L);
        transaccionEntitySaved.setTipoTransaccion(TipoTransaccion.CONSIGNACION);
        transaccionEntitySaved.setMonto(new BigDecimal("500.00"));
        transaccionEntitySaved.setDescripcion("Consignación de prueba");
        transaccionEntitySaved.setCuentaOrigen(cuentaOrigen);
        transaccionEntitySaved.setCuentaDestino(cuentaDestino);
        transaccionEntitySaved.setNumeroCuentaOrigen("5312345678");
        transaccionEntitySaved.setNumeroCuentaDestino("3387654321");
        transaccionEntitySaved.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void save_DeberiaGuardarTransaccionConCuentaOrigenYDestino() {
        when(transaccionMapper.toEntity(transaccionDomain)).thenReturn(transaccionEntity);
        when(productoJpaRepository.findById(1L)).thenReturn(Optional.of(cuentaOrigen));
        when(productoJpaRepository.findById(2L)).thenReturn(Optional.of(cuentaDestino));
        when(transaccionJpaRepository.save(any(TransaccionEntity.class))).thenReturn(transaccionEntitySaved);
        when(transaccionMapper.toDomain(transaccionEntitySaved)).thenReturn(transaccionDomain);

        Transaccion resultado = transaccionRepositoryAdapter.save(transaccionDomain);

        assertNotNull(resultado);
        assertEquals(transaccionDomain.getId(), resultado.getId());
        assertEquals(transaccionDomain.getTipoTransaccion(), resultado.getTipoTransaccion());
        assertEquals(transaccionDomain.getMonto(), resultado.getMonto());

        verify(transaccionMapper).toEntity(transaccionDomain);
        verify(productoJpaRepository).findById(1L);
        verify(productoJpaRepository).findById(2L);
        verify(transaccionJpaRepository).save(any(TransaccionEntity.class));
        verify(transaccionMapper).toDomain(transaccionEntitySaved);
    }

    @Test
    void save_DeberiaGuardarTransaccionSoloConCuentaOrigen() {
        transaccionDomain.setCuentaDestinoId(null);
        transaccionDomain.setNumeroCuentaDestino(null);
        when(transaccionMapper.toEntity(transaccionDomain)).thenReturn(transaccionEntity);
        when(productoJpaRepository.findById(1L)).thenReturn(Optional.of(cuentaOrigen));
        when(transaccionJpaRepository.save(any(TransaccionEntity.class))).thenReturn(transaccionEntitySaved);
        when(transaccionMapper.toDomain(transaccionEntitySaved)).thenReturn(transaccionDomain);

        Transaccion resultado = transaccionRepositoryAdapter.save(transaccionDomain);

        assertNotNull(resultado);
        assertEquals(transaccionDomain.getId(), resultado.getId());

        verify(productoJpaRepository).findById(1L);
        verify(productoJpaRepository, never()).findById(2L);
        verify(transaccionJpaRepository).save(any(TransaccionEntity.class));
    }

    @Test
    void save_DeberiaGuardarTransaccionSoloConCuentaDestino() {
        transaccionDomain.setCuentaOrigenId(null);
        transaccionDomain.setNumeroCuentaOrigen(null);
        when(transaccionMapper.toEntity(transaccionDomain)).thenReturn(transaccionEntity);
        when(productoJpaRepository.findById(2L)).thenReturn(Optional.of(cuentaDestino));
        when(transaccionJpaRepository.save(any(TransaccionEntity.class))).thenReturn(transaccionEntitySaved);
        when(transaccionMapper.toDomain(transaccionEntitySaved)).thenReturn(transaccionDomain);

        Transaccion resultado = transaccionRepositoryAdapter.save(transaccionDomain);

        assertNotNull(resultado);
        assertEquals(transaccionDomain.getId(), resultado.getId());

        verify(productoJpaRepository, never()).findById(1L);
        verify(productoJpaRepository).findById(2L);
        verify(transaccionJpaRepository).save(any(TransaccionEntity.class));
    }

    @Test
    void save_DeberiaGuardarTransaccionSinCuentas() {
        transaccionDomain.setCuentaOrigenId(null);
        transaccionDomain.setCuentaDestinoId(null);
        transaccionDomain.setNumeroCuentaOrigen(null);
        transaccionDomain.setNumeroCuentaDestino(null);
        when(transaccionMapper.toEntity(transaccionDomain)).thenReturn(transaccionEntity);
        when(transaccionJpaRepository.save(any(TransaccionEntity.class))).thenReturn(transaccionEntitySaved);
        when(transaccionMapper.toDomain(transaccionEntitySaved)).thenReturn(transaccionDomain);

        Transaccion resultado = transaccionRepositoryAdapter.save(transaccionDomain);

        assertNotNull(resultado);
        assertEquals(transaccionDomain.getId(), resultado.getId());

        verify(productoJpaRepository, never()).findById(any());
        verify(transaccionJpaRepository).save(any(TransaccionEntity.class));
    }

    @Test
    void save_DeberiaGuardarTransaccionCuandoCuentaNoExiste() {
        Long cuentaInexistente = 999L;
        transaccionDomain.setCuentaOrigenId(cuentaInexistente);
        when(transaccionMapper.toEntity(transaccionDomain)).thenReturn(transaccionEntity);
        when(productoJpaRepository.findById(cuentaInexistente)).thenReturn(Optional.empty());
        when(transaccionJpaRepository.save(any(TransaccionEntity.class))).thenReturn(transaccionEntitySaved);
        when(transaccionMapper.toDomain(transaccionEntitySaved)).thenReturn(transaccionDomain);

        Transaccion resultado = transaccionRepositoryAdapter.save(transaccionDomain);

        assertNotNull(resultado);
        verify(productoJpaRepository).findById(cuentaInexistente);
        verify(transaccionJpaRepository).save(any(TransaccionEntity.class));
    }

    @Test
    void findById_DeberiaRetornarTransaccionCuandoExiste() {
        Long id = 1L;
        when(transaccionJpaRepository.findById(id)).thenReturn(Optional.of(transaccionEntitySaved));
        when(transaccionMapper.toDomain(transaccionEntitySaved)).thenReturn(transaccionDomain);

        Optional<Transaccion> resultado = transaccionRepositoryAdapter.findById(id);

        assertTrue(resultado.isPresent());
        assertEquals(transaccionDomain.getId(), resultado.get().getId());
        assertEquals(transaccionDomain.getTipoTransaccion(), resultado.get().getTipoTransaccion());
        verify(transaccionJpaRepository).findById(id);
        verify(transaccionMapper).toDomain(transaccionEntitySaved);
    }

    @Test
    void findById_DeberiaRetornarEmptyCuandoNoExiste() {
        Long id = 999L;
        when(transaccionJpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Transaccion> resultado = transaccionRepositoryAdapter.findById(id);

        assertTrue(resultado.isEmpty());
        verify(transaccionJpaRepository).findById(id);
        verify(transaccionMapper, never()).toDomain(any());
    }

    @Test
    void findByCuentaOrigenId_DeberiaRetornarListaDeTransacciones() {
        Long cuentaOrigenId = 1L;
        TransaccionEntity entity2 = new TransaccionEntity();
        entity2.setId(2L);
        entity2.setTipoTransaccion(TipoTransaccion.RETIRO);
        entity2.setMonto(new BigDecimal("200.00"));

        Transaccion transaccion2 = new Transaccion();
        transaccion2.setId(2L);
        transaccion2.setTipoTransaccion(TipoTransaccion.RETIRO);
        transaccion2.setMonto(new BigDecimal("200.00"));

        List<TransaccionEntity> entities = Arrays.asList(transaccionEntitySaved, entity2);
        when(transaccionJpaRepository.findByCuentaOrigenId(cuentaOrigenId)).thenReturn(entities);
        when(transaccionMapper.toDomain(transaccionEntitySaved)).thenReturn(transaccionDomain);
        when(transaccionMapper.toDomain(entity2)).thenReturn(transaccion2);

        List<Transaccion> resultado = transaccionRepositoryAdapter.findByCuentaOrigenId(cuentaOrigenId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(transaccionDomain.getId(), resultado.get(0).getId());
        assertEquals(transaccion2.getId(), resultado.get(1).getId());
        verify(transaccionJpaRepository).findByCuentaOrigenId(cuentaOrigenId);
        verify(transaccionMapper, times(2)).toDomain(any(TransaccionEntity.class));
    }

    @Test
    void findByCuentaOrigenId_DeberiaRetornarListaVaciaCuandoNoHayTransacciones() {
        Long cuentaOrigenId = 999L;
        when(transaccionJpaRepository.findByCuentaOrigenId(cuentaOrigenId)).thenReturn(Arrays.asList());

        List<Transaccion> resultado = transaccionRepositoryAdapter.findByCuentaOrigenId(cuentaOrigenId);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(transaccionJpaRepository).findByCuentaOrigenId(cuentaOrigenId);
        verify(transaccionMapper, never()).toDomain(any());
    }

    @Test
    void findByCuentaDestinoId_DeberiaRetornarListaDeTransacciones() {
        Long cuentaDestinoId = 2L;
        TransaccionEntity entity2 = new TransaccionEntity();
        entity2.setId(2L);
        entity2.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA);
        entity2.setMonto(new BigDecimal("300.00"));

        Transaccion transaccion2 = new Transaccion();
        transaccion2.setId(2L);
        transaccion2.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA);
        transaccion2.setMonto(new BigDecimal("300.00"));

        List<TransaccionEntity> entities = Arrays.asList(transaccionEntitySaved, entity2);
        when(transaccionJpaRepository.findByCuentaDestinoId(cuentaDestinoId)).thenReturn(entities);
        when(transaccionMapper.toDomain(transaccionEntitySaved)).thenReturn(transaccionDomain);
        when(transaccionMapper.toDomain(entity2)).thenReturn(transaccion2);

        List<Transaccion> resultado = transaccionRepositoryAdapter.findByCuentaDestinoId(cuentaDestinoId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(transaccionDomain.getId(), resultado.get(0).getId());
        assertEquals(transaccion2.getId(), resultado.get(1).getId());
        verify(transaccionJpaRepository).findByCuentaDestinoId(cuentaDestinoId);
        verify(transaccionMapper, times(2)).toDomain(any(TransaccionEntity.class));
    }

    @Test
    void findByCuentaDestinoId_DeberiaRetornarListaVaciaCuandoNoHayTransacciones() {
        Long cuentaDestinoId = 999L;
        when(transaccionJpaRepository.findByCuentaDestinoId(cuentaDestinoId)).thenReturn(Arrays.asList());

        List<Transaccion> resultado = transaccionRepositoryAdapter.findByCuentaDestinoId(cuentaDestinoId);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(transaccionJpaRepository).findByCuentaDestinoId(cuentaDestinoId);
        verify(transaccionMapper, never()).toDomain(any());
    }

    @Test
    void findAll_DeberiaRetornarListaDeTransacciones() {
        TransaccionEntity entity2 = new TransaccionEntity();
        entity2.setId(2L);
        entity2.setTipoTransaccion(TipoTransaccion.RETIRO);

        Transaccion transaccion2 = new Transaccion();
        transaccion2.setId(2L);
        transaccion2.setTipoTransaccion(TipoTransaccion.RETIRO);

        List<TransaccionEntity> entities = Arrays.asList(transaccionEntitySaved, entity2);
        when(transaccionJpaRepository.findAll()).thenReturn(entities);
        when(transaccionMapper.toDomain(transaccionEntitySaved)).thenReturn(transaccionDomain);
        when(transaccionMapper.toDomain(entity2)).thenReturn(transaccion2);

        List<Transaccion> resultado = transaccionRepositoryAdapter.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(transaccionDomain.getId(), resultado.get(0).getId());
        assertEquals(transaccion2.getId(), resultado.get(1).getId());
        verify(transaccionJpaRepository).findAll();
        verify(transaccionMapper, times(2)).toDomain(any(TransaccionEntity.class));
    }

    @Test
    void findAll_DeberiaRetornarListaVaciaCuandoNoHayTransacciones() {
        when(transaccionJpaRepository.findAll()).thenReturn(Arrays.asList());

        List<Transaccion> resultado = transaccionRepositoryAdapter.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(transaccionJpaRepository).findAll();
        verify(transaccionMapper, never()).toDomain(any());
    }

    @Test
    void findByNumeroCuentaOrigen_DeberiaRetornarListaDeTransacciones() {
        String numeroCuentaOrigen = "5312345678";
        TransaccionEntity entity2 = new TransaccionEntity();
        entity2.setId(2L);
        entity2.setTipoTransaccion(TipoTransaccion.RETIRO);
        entity2.setNumeroCuentaOrigen(numeroCuentaOrigen);

        Transaccion transaccion2 = new Transaccion();
        transaccion2.setId(2L);
        transaccion2.setTipoTransaccion(TipoTransaccion.RETIRO);
        transaccion2.setNumeroCuentaOrigen(numeroCuentaOrigen);

        List<TransaccionEntity> entities = Arrays.asList(transaccionEntitySaved, entity2);
        when(transaccionJpaRepository.findByNumeroCuentaOrigen(numeroCuentaOrigen)).thenReturn(entities);
        when(transaccionMapper.toDomain(transaccionEntitySaved)).thenReturn(transaccionDomain);
        when(transaccionMapper.toDomain(entity2)).thenReturn(transaccion2);

        List<Transaccion> resultado = transaccionRepositoryAdapter.findByNumeroCuentaOrigen(numeroCuentaOrigen);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(transaccionDomain.getNumeroCuentaOrigen(), resultado.get(0).getNumeroCuentaOrigen());
        assertEquals(transaccion2.getNumeroCuentaOrigen(), resultado.get(1).getNumeroCuentaOrigen());
        verify(transaccionJpaRepository).findByNumeroCuentaOrigen(numeroCuentaOrigen);
        verify(transaccionMapper, times(2)).toDomain(any(TransaccionEntity.class));
    }

    @Test
    void findByNumeroCuentaOrigen_DeberiaRetornarListaVaciaCuandoNoHayTransacciones() {
        String numeroCuentaOrigen = "9999999999";
        when(transaccionJpaRepository.findByNumeroCuentaOrigen(numeroCuentaOrigen)).thenReturn(Arrays.asList());

        List<Transaccion> resultado = transaccionRepositoryAdapter.findByNumeroCuentaOrigen(numeroCuentaOrigen);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(transaccionJpaRepository).findByNumeroCuentaOrigen(numeroCuentaOrigen);
        verify(transaccionMapper, never()).toDomain(any());
    }

    @Test
    void findByNumeroCuentaDestino_DeberiaRetornarListaDeTransacciones() {
        String numeroCuentaDestino = "3387654321";
        TransaccionEntity entity2 = new TransaccionEntity();
        entity2.setId(2L);
        entity2.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA);
        entity2.setNumeroCuentaDestino(numeroCuentaDestino);

        Transaccion transaccion2 = new Transaccion();
        transaccion2.setId(2L);
        transaccion2.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA);
        transaccion2.setNumeroCuentaDestino(numeroCuentaDestino);

        List<TransaccionEntity> entities = Arrays.asList(transaccionEntitySaved, entity2);
        when(transaccionJpaRepository.findByNumeroCuentaDestino(numeroCuentaDestino)).thenReturn(entities);
        when(transaccionMapper.toDomain(transaccionEntitySaved)).thenReturn(transaccionDomain);
        when(transaccionMapper.toDomain(entity2)).thenReturn(transaccion2);

        List<Transaccion> resultado = transaccionRepositoryAdapter.findByNumeroCuentaDestino(numeroCuentaDestino);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(transaccionDomain.getNumeroCuentaDestino(), resultado.get(0).getNumeroCuentaDestino());
        assertEquals(transaccion2.getNumeroCuentaDestino(), resultado.get(1).getNumeroCuentaDestino());
        verify(transaccionJpaRepository).findByNumeroCuentaDestino(numeroCuentaDestino);
        verify(transaccionMapper, times(2)).toDomain(any(TransaccionEntity.class));
    }

    @Test
    void findByNumeroCuentaDestino_DeberiaRetornarListaVaciaCuandoNoHayTransacciones() {
        String numeroCuentaDestino = "9999999999";
        when(transaccionJpaRepository.findByNumeroCuentaDestino(numeroCuentaDestino)).thenReturn(Arrays.asList());

        List<Transaccion> resultado = transaccionRepositoryAdapter.findByNumeroCuentaDestino(numeroCuentaDestino);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(transaccionJpaRepository).findByNumeroCuentaDestino(numeroCuentaDestino);
        verify(transaccionMapper, never()).toDomain(any());
    }
}