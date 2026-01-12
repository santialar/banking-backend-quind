package com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence;

import com.prueba.bankingbackendquind.domain.model.Cliente;
import com.prueba.bankingbackendquind.domain.model.enums.TipoIdentificacion;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.ClienteEntity;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.mapper.ClienteMapper;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.repository.ClienteJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteRepositoryAdapterTest {

    @Mock
    private ClienteJpaRepository clienteJpaRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteRepositoryAdapter clienteRepositoryAdapter;

    private Cliente clienteDomain;
    private ClienteEntity clienteEntity;
    private ClienteEntity clienteEntitySaved;

    @BeforeEach
    void setUp() {
        clienteDomain = new Cliente();
        clienteDomain.setId(1L);
        clienteDomain.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
        clienteDomain.setNumeroIdentificacion("1234567890");
        clienteDomain.setNombres("Juan Carlos");
        clienteDomain.setApellido("Pérez");
        clienteDomain.setCorreoElectronico("juan.perez@example.com");
        clienteDomain.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        clienteDomain.setFechaCreacion(LocalDateTime.now());
        clienteDomain.setFechaModificacion(LocalDateTime.now());

        clienteEntity = new ClienteEntity();
        clienteEntity.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
        clienteEntity.setNumeroIdentificacion("1234567890");
        clienteEntity.setNombres("Juan Carlos");
        clienteEntity.setApellido("Pérez");
        clienteEntity.setCorreoElectronico("juan.perez@example.com");
        clienteEntity.setFechaNacimiento(LocalDate.of(1990, 5, 15));

        clienteEntitySaved = new ClienteEntity();
        clienteEntitySaved.setId(1L);
        clienteEntitySaved.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
        clienteEntitySaved.setNumeroIdentificacion("1234567890");
        clienteEntitySaved.setNombres("Juan Carlos");
        clienteEntitySaved.setApellido("Pérez");
        clienteEntitySaved.setCorreoElectronico("juan.perez@example.com");
        clienteEntitySaved.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        clienteEntitySaved.setFechaCreacion(LocalDateTime.now());
        clienteEntitySaved.setFechaModificacion(LocalDateTime.now());
    }

    @Test
    void save_DeberiaGuardarClienteYRetornarClienteDomain() {
        when(clienteMapper.toEntity(clienteDomain)).thenReturn(clienteEntity);
        when(clienteJpaRepository.save(clienteEntity)).thenReturn(clienteEntitySaved);
        when(clienteMapper.toDomain(clienteEntitySaved)).thenReturn(clienteDomain);

        Cliente resultado = clienteRepositoryAdapter.save(clienteDomain);

        assertNotNull(resultado);
        assertEquals(clienteDomain.getId(), resultado.getId());
        assertEquals(clienteDomain.getNumeroIdentificacion(), resultado.getNumeroIdentificacion());

        verify(clienteMapper).toEntity(clienteDomain);
        verify(clienteJpaRepository).save(clienteEntity);
        verify(clienteMapper).toDomain(clienteEntitySaved);
    }

    @Test
    void findById_DeberiaRetornarClienteCuandoExiste() {
        Long id = 1L;
        when(clienteJpaRepository.findById(id)).thenReturn(Optional.of(clienteEntitySaved));
        when(clienteMapper.toDomain(clienteEntitySaved)).thenReturn(clienteDomain);

        Optional<Cliente> resultado = clienteRepositoryAdapter.findById(id);

        assertTrue(resultado.isPresent());
        assertEquals(clienteDomain.getId(), resultado.get().getId());
        assertEquals(clienteDomain.getNumeroIdentificacion(), resultado.get().getNumeroIdentificacion());
        verify(clienteJpaRepository).findById(id);
        verify(clienteMapper).toDomain(clienteEntitySaved);
    }

    @Test
    void findById_DeberiaRetornarEmptyCuandoNoExiste() {
        Long id = 999L;
        when(clienteJpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteRepositoryAdapter.findById(id);

        assertTrue(resultado.isEmpty());
        verify(clienteJpaRepository).findById(id);
        verify(clienteMapper, never()).toDomain(any());
    }

    @Test
    void findByNumeroIdentificacion_DeberiaRetornarClienteCuandoExiste() {
        String numeroIdentificacion = "1234567890";
        when(clienteJpaRepository.findByNumeroIdentificacion(numeroIdentificacion))
                .thenReturn(Optional.of(clienteEntitySaved));
        when(clienteMapper.toDomain(clienteEntitySaved)).thenReturn(clienteDomain);

        Optional<Cliente> resultado = clienteRepositoryAdapter.findByNumeroIdentificacion(numeroIdentificacion);

        assertTrue(resultado.isPresent());
        assertEquals(clienteDomain.getNumeroIdentificacion(), resultado.get().getNumeroIdentificacion());
        verify(clienteJpaRepository).findByNumeroIdentificacion(numeroIdentificacion);
        verify(clienteMapper).toDomain(clienteEntitySaved);
    }

    @Test
    void findByNumeroIdentificacion_DeberiaRetornarEmptyCuandoNoExiste() {
        String numeroIdentificacion = "9999999999";
        when(clienteJpaRepository.findByNumeroIdentificacion(numeroIdentificacion))
                .thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteRepositoryAdapter.findByNumeroIdentificacion(numeroIdentificacion);

        assertTrue(resultado.isEmpty());
        verify(clienteJpaRepository).findByNumeroIdentificacion(numeroIdentificacion);
        verify(clienteMapper, never()).toDomain(any());
    }

    @Test
    void findAll_DeberiaRetornarListaDeClientes() {
        ClienteEntity entity2 = new ClienteEntity();
        entity2.setId(2L);
        entity2.setNumeroIdentificacion("9876543210");
        entity2.setNombres("María");
        entity2.setApellido("González");

        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNumeroIdentificacion("9876543210");
        cliente2.setNombres("María");
        cliente2.setApellido("González");

        List<ClienteEntity> entities = Arrays.asList(clienteEntitySaved, entity2);
        when(clienteJpaRepository.findAll()).thenReturn(entities);
        when(clienteMapper.toDomain(clienteEntitySaved)).thenReturn(clienteDomain);
        when(clienteMapper.toDomain(entity2)).thenReturn(cliente2);

        List<Cliente> resultado = clienteRepositoryAdapter.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(clienteDomain.getId(), resultado.get(0).getId());
        assertEquals(cliente2.getId(), resultado.get(1).getId());
        verify(clienteJpaRepository).findAll();
        verify(clienteMapper, times(2)).toDomain(any(ClienteEntity.class));
    }

    @Test
    void findAll_DeberiaRetornarListaVaciaCuandoNoHayClientes() {
        when(clienteJpaRepository.findAll()).thenReturn(Arrays.asList());

        List<Cliente> resultado = clienteRepositoryAdapter.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(clienteJpaRepository).findAll();
        verify(clienteMapper, never()).toDomain(any());
    }

    @Test
    void deleteById_DeberiaEliminarCliente() {
        Long id = 1L;
        doNothing().when(clienteJpaRepository).deleteById(id);

        clienteRepositoryAdapter.deleteById(id);

        verify(clienteJpaRepository).deleteById(id);
    }

    @Test
    void existsByNumeroIdentificacion_DeberiaRetornarTrueCuandoExiste() {
        String numeroIdentificacion = "1234567890";
        when(clienteJpaRepository.existsByNumeroIdentificacion(numeroIdentificacion)).thenReturn(true);

        boolean resultado = clienteRepositoryAdapter.existsByNumeroIdentificacion(numeroIdentificacion);

        assertTrue(resultado);
        verify(clienteJpaRepository).existsByNumeroIdentificacion(numeroIdentificacion);
    }

    @Test
    void existsByNumeroIdentificacion_DeberiaRetornarFalseCuandoNoExiste() {
        String numeroIdentificacion = "9999999999";
        when(clienteJpaRepository.existsByNumeroIdentificacion(numeroIdentificacion)).thenReturn(false);

        boolean resultado = clienteRepositoryAdapter.existsByNumeroIdentificacion(numeroIdentificacion);

        assertFalse(resultado);
        verify(clienteJpaRepository).existsByNumeroIdentificacion(numeroIdentificacion);
    }
}