package com.prueba.bankingbackendquind.application.service;


import com.prueba.bankingbackendquind.domain.exception.ProductoNotFoundException;
import com.prueba.bankingbackendquind.domain.exception.TransaccionException;
import com.prueba.bankingbackendquind.domain.model.Producto;
import com.prueba.bankingbackendquind.domain.model.Transaccion;
import com.prueba.bankingbackendquind.domain.model.enums.EstadoCuenta;
import com.prueba.bankingbackendquind.domain.model.enums.TipoTransaccion;
import com.prueba.bankingbackendquind.domain.port.in.TransaccionUseCase;
import com.prueba.bankingbackendquind.domain.port.out.ProductoRepositoryPort;
import com.prueba.bankingbackendquind.domain.port.out.TransaccionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TransaccionService implements TransaccionUseCase {

    private final TransaccionRepositoryPort transaccionRepository;
    private final ProductoRepositoryPort productoRepository;

    public TransaccionService(TransaccionRepositoryPort transaccionRepository,
                              ProductoRepositoryPort productoRepository) {
        this.transaccionRepository = transaccionRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public Transaccion realizarConsignacion(String numeroCuenta, BigDecimal monto, String descripcion) {
        validarMonto(monto);

        Producto cuenta = obtenerCuentaActiva(numeroCuenta);

        cuenta.actualizarSaldo(monto);
        productoRepository.save(cuenta);

        Transaccion transaccion = new Transaccion(
                TipoTransaccion.CONSIGNACION,
                monto,
                descripcion,
                cuenta.getId(),
                numeroCuenta
        );

        return transaccionRepository.save(transaccion);
    }

    @Override
    public Transaccion realizarRetiro(String numeroCuenta, BigDecimal monto, String descripcion) {
        validarMonto(monto);

        Producto cuenta = obtenerCuentaActiva(numeroCuenta);

        if (!cuenta.puedeRetirar(monto)) {
            throw new TransaccionException("Saldo insuficiente para realizar el retiro");
        }

        cuenta.actualizarSaldo(monto.negate());
        productoRepository.save(cuenta);

        Transaccion transaccion = new Transaccion(
                TipoTransaccion.RETIRO,
                monto,
                descripcion,
                cuenta.getId(),
                numeroCuenta
        );

        return transaccionRepository.save(transaccion);
    }

    @Override
    public Transaccion realizarTransferencia(String numeroCuentaOrigen, String numeroCuentaDestino,
                                             BigDecimal monto, String descripcion) {
        validarMonto(monto);

        if (numeroCuentaOrigen.equals(numeroCuentaDestino)) {
            throw new TransaccionException("No se puede transferir a la misma cuenta");
        }

        Producto cuentaOrigen = obtenerCuentaActiva(numeroCuentaOrigen);
        Producto cuentaDestino = obtenerCuentaActiva(numeroCuentaDestino);

        if (!cuentaOrigen.puedeRetirar(monto)) {
            throw new TransaccionException("Saldo insuficiente en la cuenta origen para realizar la transferencia");
        }

        cuentaOrigen.actualizarSaldo(monto.negate());
        cuentaDestino.actualizarSaldo(monto);

        productoRepository.save(cuentaOrigen);
        productoRepository.save(cuentaDestino);

        Transaccion transaccion = new Transaccion(
                TipoTransaccion.TRANSFERENCIA,
                monto,
                descripcion,
                cuentaOrigen.getId(),
                cuentaDestino.getId(),
                numeroCuentaOrigen,
                numeroCuentaDestino
        );

        return transaccionRepository.save(transaccion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaccion> obtenerTransaccionesPorCuenta(String numeroCuenta) {
        List<Transaccion> transacciones = new ArrayList<>();

        transacciones.addAll(transaccionRepository.findByNumeroCuentaOrigen(numeroCuenta));

        transacciones.addAll(transaccionRepository.findByNumeroCuentaDestino(numeroCuenta));

        transacciones.sort((t1, t2) -> t2.getFechaCreacion().compareTo(t1.getFechaCreacion()));

        return transacciones;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaccion> obtenerTodasLasTransacciones() {
        return transaccionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Transaccion obtenerTransaccionPorId(Long id) {
        return transaccionRepository.findById(id)
                .orElseThrow(() -> new TransaccionException("Transacción no encontrada con ID: " + id));
    }

    private void validarMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransaccionException("El monto debe ser mayor a cero");
        }
    }

    private Producto obtenerCuentaActiva(String numeroCuenta) {
        Producto cuenta = productoRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ProductoNotFoundException("Cuenta no encontrada: " + numeroCuenta));

        if (cuenta.getEstado() != EstadoCuenta.ACTIVA) {
            throw new TransaccionException("La cuenta debe estar activa para realizar transacciones");
        }

        return cuenta;
    }
}
