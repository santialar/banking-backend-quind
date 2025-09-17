package com.prueba.bankingbackendquind.domain.model.enums;

public enum TipoTransaccion {
    CONSIGNACION("Consignación"),
    RETIRO("Retiro"),
    TRANSFERENCIA("Transferencia");

    private final String descripcion;

    TipoTransaccion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
