package com.prueba.bankingbackendquind.domain.port.in;


import com.prueba.bankingbackendquind.domain.model.Cliente;

import java.util.List;

public interface ClienteUseCase {
    Cliente crearCliente(Cliente cliente);

    Cliente actualizarCliente(Long id, Cliente cliente);

    Cliente obtenerClientePorId(Long id);

    Cliente obtenerClientePorNumeroIdentificacion(String numeroIdentificacion);

    List<Cliente> obtenerTodosLosClientes();

    void eliminarCliente(Long id);
}
