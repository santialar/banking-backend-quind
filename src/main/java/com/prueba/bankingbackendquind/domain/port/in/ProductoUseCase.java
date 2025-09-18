package com.prueba.bankingbackendquind.domain.port.in;


import com.prueba.bankingbackendquind.domain.model.Producto;
import com.prueba.bankingbackendquind.domain.model.enums.EstadoCuenta;
import com.prueba.bankingbackendquind.domain.model.enums.TipoCuenta;

import java.util.List;

public interface ProductoUseCase {
    Producto crearProducto(TipoCuenta tipoCuenta, Long clienteId, boolean exentaGMF);

    Producto obtenerProductoPorId(Long id);

    Producto obtenerProductoPorNumeroCuenta(String numeroCuenta);

    List<Producto> obtenerProductosPorCliente(Long clienteId);

    List<Producto> obtenerTodosLosProductos();

    Producto cambiarEstadoProducto(Long id, EstadoCuenta nuevoEstado);

    void eliminarProducto(Long id);
}
