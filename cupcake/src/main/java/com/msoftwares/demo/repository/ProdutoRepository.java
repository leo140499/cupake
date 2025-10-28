package com.msoftwares.demo.repository;

import com.msoftwares.demo.model.Cliente;
import com.msoftwares.demo.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository  extends JpaRepository<Produto, Long> {
}
