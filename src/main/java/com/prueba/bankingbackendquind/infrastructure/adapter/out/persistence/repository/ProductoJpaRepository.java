package com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.repository;

import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoJpaRepository extends JpaRepository<ProductoEntity, Long> {
    Optional<ProductoEntity> findByNumeroCuenta(String numeroCuenta);

    List<ProductoEntity> findByClienteId(Long clienteId);

    boolean existsByNumeroCuenta(String numeroCuenta);

    int countByClienteId(Long clienteId);
}
