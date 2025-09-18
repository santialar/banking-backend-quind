package com.prueba.bankingbackendquind.domain.port.out;


import com.prueba.bankingbackendquind.domain.model.Transaccion;

import java.util.List;
import java.util.Optional;

public interface TransaccionRepositoryPort {
    Transaccion save(Transaccion transaccion);

    Optional<Transaccion> findById(Long id);

    List<Transaccion> findByCuentaOrigenId(Long cuentaOrigenId);

    List<Transaccion> findByCuentaDestinoId(Long cuentaDestinoId);

    List<Transaccion> findAll();

    List<Transaccion> findByNumeroCuentaOrigen(String numeroCuentaOrigen);

    List<Transaccion> findByNumeroCuentaDestino(String numeroCuentaDestino);
}
