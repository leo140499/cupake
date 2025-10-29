package com.msoftwares.demo.controller;

import com.msoftwares.demo.model.Administrador;
import com.msoftwares.demo.model.Pedido;
import com.msoftwares.demo.service.AdminService;
import com.msoftwares.demo.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mostrarDashboard_deveAdicionarTotaisNoModelo() {
        // Arrange
        Pedido pedido1 = new Pedido();
        pedido1.setValor(BigDecimal.valueOf(20.50));

        Pedido pedido2 = new Pedido();
        pedido2.setValor(BigDecimal.valueOf(30.00));

        List<Pedido> pedidos = Arrays.asList(pedido1, pedido2);

        when(pedidoService.listar()).thenReturn(pedidos);
        when(session.getAttribute("adminLogado")).thenReturn(new Administrador());

        // Act
        String viewName = adminController.mostrarDashboard(model, session);

        // Assert
        verify(model).addAttribute("totalVendas", BigDecimal.valueOf(50.50));
        verify(model).addAttribute("totalPedidos", 2L);
        assertEquals("pages/mainpageadmin", viewName);
    }

    @Test
    void mostrarDashboard_deveTratarListaVazia() {
        when(pedidoService.listar()).thenReturn(Collections.emptyList());

        String viewName = adminController.mostrarDashboard(model, session);

        verify(model).addAttribute("totalVendas", BigDecimal.ZERO);
        verify(model).addAttribute("totalPedidos", 0L);
        assertEquals("pages/mainpageadmin", viewName);
    }

    @Test
    void listarPedidos_deveAdicionarPedidosNoModelo() {
        List<Pedido> pedidos = List.of(new Pedido(), new Pedido());
        when(pedidoService.listar()).thenReturn(pedidos);

        String viewName = adminController.listarPedidos(model, session);

        verify(model).addAttribute("pedidos", pedidos);
        assertEquals("pages/lista-pedidos", viewName);
    }
}