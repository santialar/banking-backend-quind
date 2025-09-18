package com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.repository;

import com.prueba.bankingbackendquind.infrastructure.adapter.out.persistence.entity.TransaccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionJpaRepository extends JpaRepository<TransaccionEntity, Long> {
    List<TransaccionEntity> findByCuentaOrigenId(Long cuentaOrigenId);

    List<TransaccionEntity> findByCuentaDestinoId(Long cuentaDestinoId);

    List<TransaccionEntity> findByNumeroCuentaOrigen(String numeroCuentaOrigen);

    List<TransaccionEntity> findByNumeroCuentaDestino(String numeroCuentaDestino);
}
