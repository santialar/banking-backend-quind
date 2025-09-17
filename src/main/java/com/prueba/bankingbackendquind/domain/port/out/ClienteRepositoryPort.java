package com.prueba.bankingbackendquind.domain.port.out;


import com.prueba.bankingbackendquind.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {
    Cliente save(Cliente cliente);

    Optional<Cliente> findById(Long id);

    Optional<Cliente> findByNumeroIdentificacion(String numeroIdentificacion);

    List<Cliente> findAll();

    void deleteById(Long id);

    boolean existsByNumeroIdentificacion(String numeroIdentificacion);
}
