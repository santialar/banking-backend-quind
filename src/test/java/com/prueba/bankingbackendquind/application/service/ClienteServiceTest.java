package com.prueba.bankingbackendquind.application.service;


import com.prueba.bankingbackendquind.domain.exception.BusinessException;
import com.prueba.bankingbackendquind.domain.exception.ClienteNotFoundException;
import com.prueba.bankingbackendquind.domain.model.Cliente;
import com.prueba.bankingbackendquind.domain.model.enums.TipoIdentificacion;
import com.prueba.bankingbackendquind.domain.port.out.ClienteRepositoryPort;
import com.prueba.bankingbackendquind.domain.port.out.ProductoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepositoryPort clienteRepository;

    @Mock
    private ProductoRepositoryPort productoRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clienteValido;

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
    }

    @Test
    void crearClienteDeberiaCrearClienteExitosamente() {
        when(clienteRepository.existsByNumeroIdentificacion(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteValido);

        Cliente resultado = clienteService.crearCliente(clienteValido);

        assertNotNull(resultado);
        assertEquals(clienteValido.getId(), resultado.getId());
        assertEquals(clienteValido.getNombres(), resultado.getNombres());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void crearClienteDeberiaLanzarExcepcionSiClienteEsMenorDeEdad() {
        clienteValido.setFechaNacimiento(LocalDate.now().minusYears(16));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> clienteService.crearCliente(clienteValido));

        assertEquals("No se puede crear un cliente menor de edad", exception.getMessage());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void crearClienteDeberiaLanzarExcepcionSiNumeroIdentificacionYaExiste() {
        when(clienteRepository.existsByNumeroIdentificacion(anyString())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> clienteService.crearCliente(clienteValido));

        assertTrue(exception.getMessage().contains("Ya existe un cliente con el número de identificación"));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void crearClienteDeberiaLanzarExcepcionSiNombreEsMuyCorto() {
        clienteValido.setNombres("A");

        BusinessException exception = assertThrows(BusinessException.class,
                () -> clienteService.crearCliente(clienteValido));

        assertEquals("El nombre debe tener al menos 2 caracteres", exception.getMessage());
    }

    @Test
    void crearClienteDeberiaLanzarExcepcionSiEmailEsInvalido() {
        clienteValido.setCorreoElectronico("email-invalido");

        BusinessException exception = assertThrows(BusinessException.class,
                () -> clienteService.crearCliente(clienteValido));

        assertEquals("El formato del correo electrónico no es válido", exception.getMessage());
    }

    @Test
    void crearClienteDeberiaLanzarExcepcionSiApellidoMuyCorto() {
        clienteValido.setApellido("A");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> clienteService.crearCliente(clienteValido));

        assertEquals("El apellido debe tener al menos 2 caracteres", ex.getMessage());
    }

    @Test
    void crearClienteDeberiaLanzarExcepcionSiApellidoEsNull() {
        clienteValido.setApellido(null);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> clienteService.crearCliente(clienteValido));

        assertEquals("El apellido debe tener al menos 2 caracteres", ex.getMessage());
    }

    @Test
    void crearClienteDeberiaLanzarExcepcionSiNumeroIdentificacionEsObligatorio() {
        clienteValido.setNumeroIdentificacion("  ");

        BusinessException ex = assertThrows(BusinessException.class,
                () -> clienteService.crearCliente(clienteValido));

        assertEquals("El número de identificación es obligatorio", ex.getMessage());
    }

    @Test
    void crearClienteDeberiaLanzarExcepcionSiNumeroIdentificacionEsNull() {
        clienteValido.setNumeroIdentificacion(null);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> clienteService.crearCliente(clienteValido));

        assertEquals("El número de identificación es obligatorio", ex.getMessage());
    }

    @Test
    void crearClienteDeberiaLanzarExcepcionSiTipoIdentificacionEsObligatorio() {
        clienteValido.setTipoIdentificacion(null);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> clienteService.crearCliente(clienteValido));

        assertEquals("El tipo de identificación es obligatorio", ex.getMessage());
    }

    @Test
    void crearClienteDeberiaLanzarExcepcionSiFechaNacimientoEsObligatoria() {
        clienteValido.setFechaNacimiento(null);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> clienteService.crearCliente(clienteValido));

        assertEquals("La fecha de nacimiento es obligatoria", ex.getMessage());
    }

    @Test
    void obtenerClientePorIdDeberiaRetornarClienteExistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteValido));

        Cliente resultado = clienteService.obtenerClientePorId(1L);

        assertNotNull(resultado);
        assertEquals(clienteValido.getId(), resultado.getId());
    }

    @Test
    void obtenerClientePorIdDeberiaLanzarExcepcionSiClienteNoExiste() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        ClienteNotFoundException exception = assertThrows(ClienteNotFoundException.class,
                () -> clienteService.obtenerClientePorId(1L));

        assertEquals("Cliente no encontrado con ID: 1", exception.getMessage());
    }

    @Test
    void obtenerTodosLosClientesDeberiaRetornarListaDeClientes() {
        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNombres("María");
        List<Cliente> clientes = Arrays.asList(clienteValido, cliente2);

        when(clienteRepository.findAll()).thenReturn(clientes);

        List<Cliente> resultado = clienteService.obtenerTodosLosClientes();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    void obtenerClientePorNumeroIdentificacionDeberiaRetornarCliente() {
        when(clienteRepository.findByNumeroIdentificacion("12345678"))
                .thenReturn(Optional.of(clienteValido));

        Cliente r = clienteService.obtenerClientePorNumeroIdentificacion("12345678");

        assertNotNull(r);
        assertEquals("12345678", r.getNumeroIdentificacion());
        verify(clienteRepository).findByNumeroIdentificacion("12345678");
    }

    @Test
    void obtenerClientePorNumeroIdentificacionDeberiaLanzarExcepcionCuandoNoExiste() {
        when(clienteRepository.findByNumeroIdentificacion("000"))
                .thenReturn(Optional.empty());

        ClienteNotFoundException ex = assertThrows(ClienteNotFoundException.class,
                () -> clienteService.obtenerClientePorNumeroIdentificacion("000"));

        assertEquals("Cliente no encontrado con número de identificación: 000", ex.getMessage());
        verify(clienteRepository).findByNumeroIdentificacion("000");
    }

    @Test
    void actualizarClienteDeberiaActualizarClienteExitosamente() {
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
        clienteActualizado.setNumeroIdentificacion("12345678");
        clienteActualizado.setNombres("Juan Carlos Actualizado");
        clienteActualizado.setApellido("Pérez");
        clienteActualizado.setCorreoElectronico("juan.nuevo@email.com");
        clienteActualizado.setFechaNacimiento(LocalDate.of(1990, 5, 15));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteValido));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActualizado);

        Cliente resultado = clienteService.actualizarCliente(1L, clienteActualizado);

        assertNotNull(resultado);
        assertEquals("Juan Carlos Actualizado", resultado.getNombres());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void actualizarClienteDeberiaActualizarClienteCuandoCambiaNumeroYNoExisteDuplicado() {
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
        clienteActualizado.setNumeroIdentificacion("99999999");
        clienteActualizado.setNombres("Juan Carlos Actualizado");
        clienteActualizado.setApellido("Pérez");
        clienteActualizado.setCorreoElectronico("juan.nuevo@email.com");
        clienteActualizado.setFechaNacimiento(LocalDate.of(1990, 5, 15));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteValido));
        when(clienteRepository.existsByNumeroIdentificacion("99999999")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteActualizado);

        Cliente resultado = clienteService.actualizarCliente(1L, clienteActualizado);

        assertNotNull(resultado);
        assertEquals("Juan Carlos Actualizado", resultado.getNombres());
        verify(clienteRepository).existsByNumeroIdentificacion("99999999");
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void actualizarClienteDeberiaLanzarExcepcionSiNuevoNumeroYaExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteValido));

        Cliente actualizado = new Cliente();
        actualizado.setTipoIdentificacion(TipoIdentificacion.CEDULA_CIUDADANIA);
        actualizado.setNumeroIdentificacion("99999999");
        actualizado.setNombres("Juan");
        actualizado.setApellido("Pérez");
        actualizado.setCorreoElectronico("juan@email.com");
        actualizado.setFechaNacimiento(LocalDate.of(1990, 5, 15));

        when(clienteRepository.existsByNumeroIdentificacion("99999999")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> clienteService.actualizarCliente(1L, actualizado));

        assertTrue(ex.getMessage().contains("Ya existe un cliente con el número de identificación"));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void eliminarClienteDeberiaEliminarClienteSinProductos() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteValido));
        when(productoRepository.countByClienteId(1L)).thenReturn(0);

        clienteService.eliminarCliente(1L);

        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void eliminarClienteDeberiaLanzarExcepcionSiTieneProductos() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteValido));
        when(productoRepository.countByClienteId(1L)).thenReturn(2);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> clienteService.eliminarCliente(1L));

        assertEquals("No se puede eliminar el cliente porque tiene productos vinculados",
                exception.getMessage());
        verify(clienteRepository, never()).deleteById(anyLong());
    }
}
