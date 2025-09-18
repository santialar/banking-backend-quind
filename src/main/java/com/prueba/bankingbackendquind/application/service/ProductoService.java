package com.prueba.bankingbackendquind.application.service;


import com.prueba.bankingbackendquind.domain.exception.BusinessException;
import com.prueba.bankingbackendquind.domain.exception.ClienteNotFoundException;
import com.prueba.bankingbackendquind.domain.exception.ProductoNotFoundException;
import com.prueba.bankingbackendquind.domain.model.Producto;
import com.prueba.bankingbackendquind.domain.model.enums.EstadoCuenta;
import com.prueba.bankingbackendquind.domain.model.enums.TipoCuenta;
import com.prueba.bankingbackendquind.domain.port.in.ProductoUseCase;
import com.prueba.bankingbackendquind.domain.port.out.ClienteRepositoryPort;
import com.prueba.bankingbackendquind.domain.port.out.ProductoRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductoService implements ProductoUseCase {

    private final ProductoRepositoryPort productoRepository;
    private final ClienteRepositoryPort clienteRepository;

    public ProductoService(ProductoRepositoryPort productoRepository,
                           ClienteRepositoryPort clienteRepository) {
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Producto crearProducto(TipoCuenta tipoCuenta, Long clienteId, boolean exentaGMF) {
        clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con ID: " + clienteId));

        Producto producto = new Producto(tipoCuenta, clienteId, exentaGMF);

        while (productoRepository.existsByNumeroCuenta(producto.getNumeroCuenta())) {
            producto = new Producto(tipoCuenta, clienteId, exentaGMF);
        }

        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Producto obtenerProductoPorNumeroCuenta(String numeroCuenta) {
        return productoRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con número de cuenta: "
                        + numeroCuenta));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosPorCliente(Long clienteId) {
        return productoRepository.findByClienteId(clienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto cambiarEstadoProducto(Long id, EstadoCuenta nuevoEstado) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));

        if (nuevoEstado == EstadoCuenta.CANCELADA && !producto.puedeSerCancelada()) {
            throw new BusinessException("No se puede cancelar una cuenta con saldo diferente a cero");
        }

        switch (nuevoEstado) {
            case ACTIVA:
                producto.activar();
                break;
            case INACTIVA:
                producto.inactivar();
                break;
            case CANCELADA:
                producto.cancelar();
                break;
            default:
                throw new BusinessException("Estado de cuenta no válido");
        }

        return productoRepository.save(producto);
    }

    @Override
    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + id));

        if (producto.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessException("No se puede eliminar un producto con saldo diferente a cero");
        }

        productoRepository.deleteById(id);
    }
}
