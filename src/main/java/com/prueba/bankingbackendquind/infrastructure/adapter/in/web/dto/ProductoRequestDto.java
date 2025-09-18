package com.prueba.bankingbackendquind.infrastructure.adapter.in.web.dto;

import com.prueba.bankingbackendquind.domain.model.enums.TipoCuenta;
import jakarta.validation.constraints.NotNull;

public class ProductoRequestDto {

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuenta tipoCuenta;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clienteId;

    private boolean exentaGMF = false;

    public ProductoRequestDto() {}

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public boolean isExentaGMF() {
        return exentaGMF;
    }

    public void setExentaGMF(boolean exentaGMF) {
        this.exentaGMF = exentaGMF;
    }
}
