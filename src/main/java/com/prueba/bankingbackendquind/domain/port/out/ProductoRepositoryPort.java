package com.prueba.bankingbackendquind.domain.port.out;


import com.prueba.bankingbackendquind.domain.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepositoryPort {
    Producto save(Producto producto);

    Optional<Producto> findById(Long id);

    Optional<Producto> findByNumeroCuenta(String numeroCuenta);

    List<Producto> findByClienteId(Long clienteId);

    List<Producto> findAll();

    void deleteById(Long id);

    boolean existsByNumeroCuenta(String numeroCuenta);

    int countByClienteId(Long clienteId);
}
