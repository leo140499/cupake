package com.msoftwares.demo.controller;

import com.msoftwares.demo.model.Administrador;
import com.msoftwares.demo.model.Cliente;
import com.msoftwares.demo.model.Pedido;
import com.msoftwares.demo.service.AdminService;
import com.msoftwares.demo.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PedidoService pedidoService;


    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model, HttpSession session) {
        // Exemplos de dados fictícios (depois tu puxa do banco com um service)
        List<Pedido> pedidos = pedidoService.listar();
        model.addAttribute("totalVendas", pedidos.stream().map((Pedido::getValor))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        model.addAttribute("totalPedidos", pedidos.stream().count());
        Administrador admin = (Administrador) session.getAttribute("adminLogado");
        System.out.println(admin);
        return "pages/mainpageadmin";
    }

    @GetMapping("/listar-pedidos")
    public String listarPedidos(Model model, HttpSession session) {
        List<Pedido> pedidos = pedidoService.listar();
        model.addAttribute("pedidos", pedidos);
        return "pages/lista-pedidos";
    }
}
