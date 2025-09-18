package com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence;


import com.prueba.bankingbackendquind.domain.model.Producto;
import com.prueba.bankingbackendquind.domain.port.out.ProductoRepositoryPort;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.ClienteEntity;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.mapper.ProductoMapper;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.repository.ClienteJpaRepository;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.repository.ProductoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductoRepositoryAdapter implements ProductoRepositoryPort {

    private final ProductoJpaRepository productoJpaRepository;
    private final ClienteJpaRepository clienteJpaRepository;
    private final ProductoMapper productoMapper;

    public ProductoRepositoryAdapter(ProductoJpaRepository productoJpaRepository,
                                   ClienteJpaRepository clienteJpaRepository,
                                   ProductoMapper productoMapper) {
        this.productoJpaRepository = productoJpaRepository;
        this.clienteJpaRepository = clienteJpaRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    public Producto save(Producto producto) {
        ProductoEntity entity = productoMapper.toEntity(producto);

        if (producto.getClienteId() != null) {
            ClienteEntity cliente = clienteJpaRepository.findById(producto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            entity.setCliente(cliente);
        }

        ProductoEntity savedEntity = productoJpaRepository.save(entity);
        return productoMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return productoJpaRepository.findById(id)
                .map(productoMapper::toDomain);
    }

    @Override
    public Optional<Producto> findByNumeroCuenta(String numeroCuenta) {
        return productoJpaRepository.findByNumeroCuenta(numeroCuenta)
                .map(productoMapper::toDomain);
    }

    @Override
    public List<Producto> findByClienteId(Long clienteId) {
        return productoJpaRepository.findByClienteId(clienteId)
                .stream()
                .map(productoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> findAll() {
        return productoJpaRepository.findAll()
                .stream()
                .map(productoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        productoJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByNumeroCuenta(String numeroCuenta) {
        return productoJpaRepository.existsByNumeroCuenta(numeroCuenta);
    }

    @Override
    public int countByClienteId(Long clienteId) {
        return productoJpaRepository.countByClienteId(clienteId);
    }
}
