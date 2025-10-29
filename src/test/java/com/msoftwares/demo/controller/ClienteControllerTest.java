package com.msoftwares.demo.controller;

import com.msoftwares.demo.model.Cliente;
import com.msoftwares.demo.model.DadosPessoais;
import com.msoftwares.demo.model.Pedido;
import com.msoftwares.demo.service.ClienteService;
import com.msoftwares.demo.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mostrarDashboard_deveAdicionarClienteNoModelo() {
        // Arrange
        Cliente cliente = new Cliente();
        DadosPessoais dados = new DadosPessoais();
        cliente.setDadosPessoais(dados);

        when(session.getAttribute("clienteLogado")).thenReturn(cliente);

        // Act
        String viewName = clienteController.mostrarDashboard(model, session);

        // Assert
        verify(model).addAttribute("cliente", dados);
        assertEquals("pages/mainpagecliente", viewName);
    }

    @Test
    void listarPedidosDoCliente_deveAdicionarPedidosNoModelo() {
        // Arrange
        Cliente cliente = new Cliente();
        List<Pedido> pedidos = List.of(new Pedido(), new Pedido());

        when(session.getAttribute("clienteLogado")).thenReturn(cliente);
        when(pedidoService.buscarPedidosPorCliente(cliente)).thenReturn(pedidos);

        // Act
        String viewName = clienteController.listarPedidosDoCliente(model, session);

        // Assert
        verify(model).addAttribute("pedidos", pedidos);
        assertEquals("pages/lista-pedidos-cliente", viewName);
    }
}
