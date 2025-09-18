package com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto;

import com.prueba.bankingbackendquind.domain.model.enums.EstadoCuenta;
import jakarta.validation.constraints.NotNull;

public class EstadoCuentaRequestDto {

    @NotNull(message = "El estado de cuenta es obligatorio")
    private EstadoCuenta estado;

    public EstadoCuentaRequestDto() {}

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }
}
