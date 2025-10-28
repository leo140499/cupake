package com.msoftwares.demo.controller;

import com.msoftwares.demo.model.*;
import com.msoftwares.demo.service.PedidoService;
import com.msoftwares.demo.service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {


    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private PedidoService pedidoService;



    @PostMapping("/carrinho/adicionar")
    public String adicionarAoCarrinho(@RequestParam Long produtoId,
                                      @RequestParam int quantidade,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        Produto produto = produtoService.buscarPorId(produtoId);

        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new Carrinho();
        }

        carrinho.adicionarItem(produto, quantidade);
        session.setAttribute("carrinho", carrinho);

        redirectAttributes.addFlashAttribute("mensagem", "Cupcake adicionado com sucesso!");
        return "redirect:/cupcakes/comprar/all";
    }

    @PostMapping("carrinho/remover")
    public String removerDoCarrinho(@RequestParam("idProduto") Long idProduto, HttpSession session, RedirectAttributes redirectAttributes) {
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");

        if (carrinho != null) {
            carrinho.removerProduto(idProduto);
            session.setAttribute("carrinho", carrinho);
            redirectAttributes.addFlashAttribute("mensagem", "Produto removido do carrinho.");
        }

        return "redirect:/cupcakes/comprar/all";
    }

    @PostMapping("carrinho/limpar")
    public String limparCarrinho(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute("carrinho");
        redirectAttributes.addFlashAttribute("mensagem", "Carrinho esvaziado com sucesso.");
        return "redirect:/cupcakes/comprar/all";
    }

    @PostMapping("/finalizar")
    public String finalizarCompra(HttpSession session, RedirectAttributes redirectAttributes) {
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");

        if (carrinho == null || carrinho.getItens().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Seu carrinho está vazio.");
            return "redirect:/cupcakes/comprar/all";
        }

        Cliente cliente = (Cliente) session.getAttribute("clienteLogado");
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("erro", "Você precisa estar logado para finalizar a compra.");
            return "redirect:/login?tipo=cliente";
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDataHora(LocalDateTime.now());
        pedido.setStatus(StatusPedido.EM_SEPARACAO);

        List<ItemPedido> itensPedido = new ArrayList<>();
        for (ItemCarrinho item : carrinho.getItens()) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProduto(item.getProduto());
            itemPedido.setQuantidade(item.getQuantidade());
            itemPedido.setPrecoUnitario(item.getProduto().getPreco());
            itemPedido.setPedido(pedido);
            itensPedido.add(itemPedido);
        }

        pedido.setItens(itensPedido);
        pedido.setValor(carrinho.calcularValorTotal());

        pedidoService.save(pedido);  // Isso já deve salvar os itens se estiver com Cascade.PERSIST

        session.removeAttribute("carrinho");
        redirectAttributes.addFlashAttribute("mensagem", "Pedido realizado com sucesso!");

        return "redirect:/pedidos/sucesso";
    }

    @GetMapping("/sucesso")
    public String mostrarDashboard() {
        return "pages/sucesso";
    }

}
