package com.msoftwares.demo.repository;

import com.msoftwares.demo.model.Administrador;
import com.msoftwares.demo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository <Administrador, Long> {
    Administrador findByDadosPessoaisEmail(String email);
    Administrador findByDadosPessoaisSenha(String senha);
    Administrador findByDadosPessoaisDocumento(String documento);


}
