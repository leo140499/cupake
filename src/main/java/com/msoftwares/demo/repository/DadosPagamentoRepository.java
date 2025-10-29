package com.msoftwares.demo.repository;

import com.msoftwares.demo.model.Administrador;
import com.msoftwares.demo.model.DadosPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DadosPagamentoRepository extends JpaRepository <DadosPagamento, Long> {
}
