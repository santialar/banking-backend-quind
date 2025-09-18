package com.prueba.bankingbackendquind.application.service;


import com.prueba.bankingbackendquind.domain.exception.BusinessException;
import com.prueba.bankingbackendquind.domain.exception.ClienteNotFoundException;
import com.prueba.bankingbackendquind.domain.model.Cliente;
import com.prueba.bankingbackendquind.domain.port.in.ClienteUseCase;
import com.prueba.bankingbackendquind.domain.port.out.ClienteRepositoryPort;
import com.prueba.bankingbackendquind.domain.port.out.ProductoRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Transactional
public class ClienteService implements ClienteUseCase {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );

    private final ClienteRepositoryPort clienteRepository;
    private final ProductoRepositoryPort productoRepository;

    public ClienteService(ClienteRepositoryPort clienteRepository,
                          ProductoRepositoryPort productoRepository) {
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public Cliente crearCliente(Cliente cliente) {
        validarCliente(cliente);

        if (clienteRepository.existsByNumeroIdentificacion(cliente.getNumeroIdentificacion())) {
            throw new BusinessException("Ya existe un cliente con el número de identificación: "
                    + cliente.getNumeroIdentificacion());
        }

        if (!cliente.esMayorDeEdad()) {
            throw new BusinessException("No se puede crear un cliente menor de edad");
        }

        cliente.setFechaCreacion(LocalDateTime.now());
        cliente.setFechaModificacion(LocalDateTime.now());

        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con ID: " + id));

        validarCliente(clienteActualizado);

        if (!clienteExistente.getNumeroIdentificacion().equals(clienteActualizado.getNumeroIdentificacion()) &&
                clienteRepository.existsByNumeroIdentificacion(clienteActualizado.getNumeroIdentificacion())) {
            throw new BusinessException("Ya existe un cliente con el número de identificación: "
                    + clienteActualizado.getNumeroIdentificacion());
        }

        clienteExistente.setTipoIdentificacion(clienteActualizado.getTipoIdentificacion());
        clienteExistente.setNumeroIdentificacion(clienteActualizado.getNumeroIdentificacion());
        clienteExistente.setNombres(clienteActualizado.getNombres());
        clienteExistente.setApellido(clienteActualizado.getApellido());
        clienteExistente.setCorreoElectronico(clienteActualizado.getCorreoElectronico());
        clienteExistente.setFechaNacimiento(clienteActualizado.getFechaNacimiento());
        clienteExistente.actualizarFechaModificacion();

        return clienteRepository.save(clienteExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente obtenerClientePorNumeroIdentificacion(String numeroIdentificacion) {
        return clienteRepository.findByNumeroIdentificacion(numeroIdentificacion)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con número de identificación: "
                        + numeroIdentificacion));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con ID: " + id));

        int cantidadProductos = productoRepository.countByClienteId(id);
        if (cantidadProductos > 0) {
            throw new BusinessException("No se puede eliminar el cliente porque tiene productos vinculados");
        }

        clienteRepository.deleteById(id);
    }

    private void validarCliente(Cliente cliente) {
        if (cliente.getNombres() == null || cliente.getNombres().trim().length() < 2) {
            throw new BusinessException("El nombre debe tener al menos 2 caracteres");
        }

        if (cliente.getApellido() == null || cliente.getApellido().trim().length() < 2) {
            throw new BusinessException("El apellido debe tener al menos 2 caracteres");
        }

        if (cliente.getCorreoElectronico() != null &&
                !EMAIL_PATTERN.matcher(cliente.getCorreoElectronico()).matches()) {
            throw new BusinessException("El formato del correo electrónico no es válido");
        }

        if (cliente.getNumeroIdentificacion() == null || cliente.getNumeroIdentificacion().trim().isEmpty()) {
            throw new BusinessException("El número de identificación es obligatorio");
        }

        if (cliente.getTipoIdentificacion() == null) {
            throw new BusinessException("El tipo de identificación es obligatorio");
        }

        if (cliente.getFechaNacimiento() == null) {
            throw new BusinessException("La fecha de nacimiento es obligatoria");
        }
    }
}
