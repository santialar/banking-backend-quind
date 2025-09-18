package com.prueba.bankingbackendquind.domain.model.enums;

public enum TipoIdentificacion {
    CEDULA_CIUDADANIA("CC", "Cédula de Ciudadanía"),
    CEDULA_EXTRANJERIA("CE", "Cédula de Extranjería"),
    PASAPORTE("PA", "Pasaporte"),
    TARJETA_IDENTIDAD("TI", "Tarjeta de Identidad");

    private final String codigo;
    private final String descripcion;

    TipoIdentificacion(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
