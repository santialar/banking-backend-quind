package com.prueba.bankingbackendquind.domain.model.enums;

public enum TipoCuenta {
    CUENTA_AHORROS("Cuenta de Ahorros"),
    CUENTA_CORRIENTE("Cuenta Corriente");

    private final String descripcion;

    TipoCuenta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
