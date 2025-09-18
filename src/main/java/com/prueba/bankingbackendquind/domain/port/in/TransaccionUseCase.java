package com.prueba.bankingbackendquind.domain.port.in;


import com.prueba.bankingbackendquind.domain.model.Transaccion;

import java.math.BigDecimal;
import java.util.List;

public interface TransaccionUseCase {
    Transaccion realizarConsignacion(String numeroCuenta, BigDecimal monto, String descripcion);

    Transaccion realizarRetiro(String numeroCuenta, BigDecimal monto, String descripcion);

    Transaccion realizarTransferencia(String numeroCuentaOrigen, String numeroCuentaDestino,
                                      BigDecimal monto, String descripcion);

    List<Transaccion> obtenerTransaccionesPorCuenta(String numeroCuenta);

    List<Transaccion> obtenerTodasLasTransacciones();

    Transaccion obtenerTransaccionPorId(Long id);
}
