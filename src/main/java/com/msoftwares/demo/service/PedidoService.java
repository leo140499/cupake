package com.msoftwares.demo.service;

import com.msoftwares.demo.dto.PedidoDTO;
import com.msoftwares.demo.model.Cliente;
import com.msoftwares.demo.model.Pedido;
import com.msoftwares.demo.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public void save (Pedido pedido){
       pedidoRepository.save(pedido);
    }

    public List<Pedido> listar(){
       return pedidoRepository.findAll();
    }

    public Pedido findPedidoById(Long id){
        return pedidoRepository.findById(id).get();
    }

    public List<Pedido> buscarPedidosPorCliente(Cliente cliente) {
        return pedidoRepository.findByClienteOrderByDataHoraDesc(cliente);
    }

}
