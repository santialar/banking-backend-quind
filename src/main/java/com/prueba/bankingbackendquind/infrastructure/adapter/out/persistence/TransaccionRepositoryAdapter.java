package com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence;


import com.prueba.bankingbackendquind.domain.model.Transaccion;
import com.prueba.bankingbackendquind.domain.port.out.TransaccionRepositoryPort;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.TransaccionEntity;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.mapper.TransaccionMapper;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.repository.ProductoJpaRepository;
import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.repository.TransaccionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TransaccionRepositoryAdapter implements TransaccionRepositoryPort {

    private final TransaccionJpaRepository transaccionJpaRepository;
    private final ProductoJpaRepository productoJpaRepository;
    private final TransaccionMapper transaccionMapper;

    public TransaccionRepositoryAdapter(TransaccionJpaRepository transaccionJpaRepository,
                                      ProductoJpaRepository productoJpaRepository,
                                      TransaccionMapper transaccionMapper) {
        this.transaccionJpaRepository = transaccionJpaRepository;
        this.productoJpaRepository = productoJpaRepository;
        this.transaccionMapper = transaccionMapper;
    }

    @Override
    public Transaccion save(Transaccion transaccion) {
        TransaccionEntity entity = transaccionMapper.toEntity(transaccion);

        if (transaccion.getCuentaOrigenId() != null) {
            ProductoEntity cuentaOrigen = productoJpaRepository.findById(transaccion.getCuentaOrigenId())
                    .orElse(null);
            entity.setCuentaOrigen(cuentaOrigen);
        }

        if (transaccion.getCuentaDestinoId() != null) {
            ProductoEntity cuentaDestino = productoJpaRepository.findById(transaccion.getCuentaDestinoId())
                    .orElse(null);
            entity.setCuentaDestino(cuentaDestino);
        }

        TransaccionEntity savedEntity = transaccionJpaRepository.save(entity);
        return transaccionMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Transaccion> findById(Long id) {
        return transaccionJpaRepository.findById(id)
                .map(transaccionMapper::toDomain);
    }

    @Override
    public List<Transaccion> findByCuentaOrigenId(Long cuentaOrigenId) {
        return transaccionJpaRepository.findByCuentaOrigenId(cuentaOrigenId)
                .stream()
                .map(transaccionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaccion> findByCuentaDestinoId(Long cuentaDestinoId) {
        return transaccionJpaRepository.findByCuentaDestinoId(cuentaDestinoId)
                .stream()
                .map(transaccionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaccion> findAll() {
        return transaccionJpaRepository.findAll()
                .stream()
                .map(transaccionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaccion> findByNumeroCuentaOrigen(String numeroCuentaOrigen) {
        return transaccionJpaRepository.findByNumeroCuentaOrigen(numeroCuentaOrigen)
                .stream()
                .map(transaccionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaccion> findByNumeroCuentaDestino(String numeroCuentaDestino) {
        return transaccionJpaRepository.findByNumeroCuentaDestino(numeroCuentaDestino)
                .stream()
                .map(transaccionMapper::toDomain)
                .collect(Collectors.toList());
    }
}
