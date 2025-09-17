package com.prueba.bankingbackendquind.domain.model;

import com.prueba.bankingbackendquind.domain.model.enums.TipoTransaccion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaccion {
    private Long id;
    private TipoTransaccion tipoTransaccion;
    private BigDecimal monto;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private Long cuentaOrigenId;
    private Long cuentaDestinoId;
    private String numeroCuentaOrigen;
    private String numeroCuentaDestino;

    public Transaccion() {
    }

    public Transaccion(TipoTransaccion tipoTransaccion, BigDecimal monto, String descripcion,
                       Long cuentaOrigenId, String numeroCuentaOrigen) {
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.descripcion = descripcion;
        this.cuentaOrigenId = cuentaOrigenId;
        this.numeroCuentaOrigen = numeroCuentaOrigen;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Transaccion(TipoTransaccion tipoTransaccion, BigDecimal monto, String descripcion,
                       Long cuentaOrigenId, Long cuentaDestinoId,
                       String numeroCuentaOrigen, String numeroCuentaDestino) {
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.descripcion = descripcion;
        this.cuentaOrigenId = cuentaOrigenId;
        this.cuentaDestinoId = cuentaDestinoId;
        this.numeroCuentaOrigen = numeroCuentaOrigen;
        this.numeroCuentaDestino = numeroCuentaDestino;
        this.fechaCreacion = LocalDateTime.now();
    }

    public boolean esTransferencia() {
        return tipoTransaccion == TipoTransaccion.TRANSFERENCIA;
    }

    public boolean esConsignacion() {
        return tipoTransaccion == TipoTransaccion.CONSIGNACION;
    }

    public boolean esRetiro() {
        return tipoTransaccion == TipoTransaccion.RETIRO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoTransaccion getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(TipoTransaccion tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getCuentaOrigenId() {
        return cuentaOrigenId;
    }

    public void setCuentaOrigenId(Long cuentaOrigenId) {
        this.cuentaOrigenId = cuentaOrigenId;
    }

    public Long getCuentaDestinoId() {
        return cuentaDestinoId;
    }

    public void setCuentaDestinoId(Long cuentaDestinoId) {
        this.cuentaDestinoId = cuentaDestinoId;
    }

    public String getNumeroCuentaOrigen() {
        return numeroCuentaOrigen;
    }

    public void setNumeroCuentaOrigen(String numeroCuentaOrigen) {
        this.numeroCuentaOrigen = numeroCuentaOrigen;
    }

    public String getNumeroCuentaDestino() {
        return numeroCuentaDestino;
    }

    public void setNumeroCuentaDestino(String numeroCuentaDestino) {
        this.numeroCuentaDestino = numeroCuentaDestino;
    }
}
