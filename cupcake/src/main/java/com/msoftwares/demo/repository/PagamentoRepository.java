package com.msoftwares.demo.repository;

import com.msoftwares.demo.model.Administrador;
import com.msoftwares.demo.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository <Pagamento, Long> {
}
