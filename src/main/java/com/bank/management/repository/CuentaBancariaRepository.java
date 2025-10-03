package com.bank.management.repository;

import com.bank.management.entity.CuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentaBancariaRepository extends JpaRepository<CuentaBancaria, Long> {
    boolean existsByNumeroCuenta(String numeroCuenta);

    boolean existsByUsuarioIdAndTipoCuenta(Long usuarioId, String tipoCuenta);
}



