package com.msoftwares.demo.repository;

import com.msoftwares.demo.model.Administrador;
import com.msoftwares.demo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository  extends JpaRepository<Cliente, Long> {
    Cliente findByDadosPessoaisEmail(String email);
    Cliente findByDadosPessoaisSenha(String senha);
    Cliente findByDadosPessoaisDocumento(String documento);

}
