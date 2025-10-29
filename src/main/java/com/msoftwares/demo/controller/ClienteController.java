package com.msoftwares.demo.controller;

import com.msoftwares.demo.model.Administrador;
import com.msoftwares.demo.model.Cliente;
import com.msoftwares.demo.model.Pedido;
import com.msoftwares.demo.service.AdminService;
import com.msoftwares.demo.service.ClienteService;
import com.msoftwares.demo.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogado");
        model.addAttribute("cliente", cliente.getDadosPessoais());
        return "pages/mainpagecliente";
    }

    @GetMapping("/listar-pedidos")
    public String listarPedidosDoCliente(Model model, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogado");
        List<Pedido> pedidos = pedidoService.buscarPedidosPorCliente(cliente);
        model.addAttribute("pedidos", pedidos);
        return "pages/lista-pedidos-cliente";
    }

}
