package com.msoftwares.demo.service;

import com.msoftwares.demo.model.Cliente;
import com.msoftwares.demo.model.Pedido;
import com.msoftwares.demo.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_deveSalvarPedido() {
        Pedido pedido = new Pedido();
        pedidoService.save(pedido);

        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    void listar_deveRetornarListaDePedidos() {
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();
        when(pedidoRepository.findAll()).thenReturn(Arrays.asList(pedido1, pedido2));

        List<Pedido> pedidos = pedidoService.listar();

        assertEquals(2, pedidos.size());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void findPedidoById_deveRetornarPedidoExistente() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.findPedidoById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void findPedidoById_deveLancarExcecaoSeNaoEncontrar() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> pedidoService.findPedidoById(1L));
    }

    @Test
    void buscarPedidosPorCliente_deveRetornarListaDePedidosDoCliente() {
        Cliente cliente = new Cliente();
        Pedido pedido = new Pedido();
        when(pedidoRepository.findByClienteOrderByDataHoraDesc(cliente)).thenReturn(List.of(pedido));

        List<Pedido> pedidos = pedidoService.buscarPedidosPorCliente(cliente);

        assertEquals(1, pedidos.size());
        verify(pedidoRepository).findByClienteOrderByDataHoraDesc(cliente);
    }
}
