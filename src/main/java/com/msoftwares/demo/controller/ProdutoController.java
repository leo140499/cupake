package com.msoftwares.demo.controller;

import com.msoftwares.demo.dto.ProdutoDTO;
import com.msoftwares.demo.model.Carrinho;
import com.msoftwares.demo.model.Produto;
import com.msoftwares.demo.service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cupcakes")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/novo")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("produto", new ProdutoDTO());
        return "pages/cadastro-cupcake";
    }

    @PostMapping("/salvar")
    public String cadastrarProduto(@ModelAttribute ProdutoDTO produto) {
        produtoService.cadastrar(produto);
        return "redirect:/cupcakes/novo";
    }

    @GetMapping("/all")
    public String listarProdutos(Model model) {
        model.addAttribute("produtos", produtoService.listar());
        return "pages/lista-cupcakes";
    }

    @GetMapping("/comprar/all")
    public String listarProdutosCliente(Model model, HttpSession session) {
        List<Produto> produtos =  produtoService.listar();
        model.addAttribute("produtos", produtos);

        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new Carrinho();
            session.setAttribute("carrinho", carrinho);
        }

        model.addAttribute("produtos", produtos);
        model.addAttribute("carrinho", carrinho);
        model.addAttribute("valorTotal", carrinho.calcularValorTotal());

        return "pages/comprar-cupcakes";
    }

    @DeleteMapping("/deletar/{id}")
    public String deletarProduto(@PathVariable Long id) {
        produtoService.deletar(id);
        return "redirect:/cupcakes/all";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCadastro(@PathVariable Long id,  Model model) {
        Produto produto = produtoService.buscarPorId(id);
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(id);
        model.addAttribute("produto", dto);
        return "pages/editar-cupcake";
    }

    @PostMapping("/salvarEdicao")
    public String editarProduto(@ModelAttribute ProdutoDTO produtoDTO) {
        produtoService.editar(produtoDTO);
        return "redirect:/cupcakes/all";
    }
}
