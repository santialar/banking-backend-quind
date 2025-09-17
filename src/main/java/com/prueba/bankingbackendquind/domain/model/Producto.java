package com.prueba.bankingbackendquind.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class Producto {
    private Long id;
    private TipoCuenta tipoCuenta;
    private String numeroCuenta;
    private EstadoCuenta estado;
    private BigDecimal saldo;
    private boolean exentaGMF;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private Long clienteId;
    private List<Transaccion> transacciones;

    public Producto() {}

    public Producto(TipoCuenta tipoCuenta, Long clienteId, boolean exentaGMF) {
        this.tipoCuenta = tipoCuenta;
        this.clienteId = clienteId;
        this.exentaGMF = exentaGMF;
        this.numeroCuenta = generarNumeroCuenta(tipoCuenta);
        this.saldo = BigDecimal.ZERO;
        this.estado = EstadoCuenta.ACTIVA;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
    }

    private String generarNumeroCuenta(TipoCuenta tipoCuenta) {
        Random random = new Random();
        String prefijo = tipoCuenta == TipoCuenta.CUENTA_AHORROS ? "53" : "33";
        StringBuilder numeroCuenta = new StringBuilder(prefijo);

        for (int i = 0; i < 8; i++) {
            numeroCuenta.append(random.nextInt(10));
        }

        return numeroCuenta.toString();
    }

    public boolean puedeSerCancelada() {
        return saldo.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean esCuentaAhorros() {
        return tipoCuenta == TipoCuenta.CUENTA_AHORROS;
    }

    public boolean puedeRetirar(BigDecimal monto) {
        if (esCuentaAhorros()) {
            return saldo.subtract(monto).compareTo(BigDecimal.ZERO) >= 0;
        }
        return true;
    }

    public void actualizarSaldo(BigDecimal monto) {
        this.saldo = this.saldo.add(monto);
        this.fechaModificacion = LocalDateTime.now();
    }

    public void activar() {
        this.estado = EstadoCuenta.ACTIVA;
        this.fechaModificacion = LocalDateTime.now();
    }

    public void inactivar() {
        this.estado = EstadoCuenta.INACTIVA;
        this.fechaModificacion = LocalDateTime.now();
    }

    public void cancelar() {
        if (puedeSerCancelada()) {
            this.estado = EstadoCuenta.CANCELADA;
            this.fechaModificacion = LocalDateTime.now();
        } else {
            throw new IllegalStateException("No se puede cancelar una cuenta con saldo diferente a cero");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public boolean isExentaGMF() {
        return exentaGMF;
    }

    public void setExentaGMF(boolean exentaGMF) {
        this.exentaGMF = exentaGMF;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }
}
