package com.msoftwares.demo.repository;

import com.msoftwares.demo.model.Administrador;
import com.msoftwares.demo.model.Cliente;
import com.msoftwares.demo.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository  extends JpaRepository <Pedido, Long> {
    List<Pedido> findByClienteOrderByDataHoraDesc(Cliente cliente);
}
