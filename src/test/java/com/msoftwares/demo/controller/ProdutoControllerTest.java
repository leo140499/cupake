package com.msoftwares.demo.controller;

import com.msoftwares.demo.dto.ProdutoDTO;
import com.msoftwares.demo.model.Carrinho;
import com.msoftwares.demo.model.Produto;
import com.msoftwares.demo.service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProdutoControllerTest {

    @InjectMocks
    private ProdutoController produtoController;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mostrarFormularioCadastro_deveRetornarViewDeCadastro() {
        String view = produtoController.mostrarFormularioCadastro(model);

        verify(model).addAttribute(eq("produto"), any(ProdutoDTO.class));
        assertEquals("pages/cadastro-cupcake", view);
    }

    @Test
    void cadastrarProduto_deveChamarServiceECriarProduto() {
        ProdutoDTO dto = new ProdutoDTO();
        String view = produtoController.cadastrarProduto(dto);

        verify(produtoService).cadastrar(dto);
        assertEquals("redirect:/cupcakes/novo", view);
    }

    @Test
    void listarProdutos_deveAdicionarProdutosAoModel() {
        List<Produto> produtos = new ArrayList<>();
        when(produtoService.listar()).thenReturn(produtos);

        String view = produtoController.listarProdutos(model);

        verify(model).addAttribute("produtos", produtos);
        assertEquals("pages/lista-cupcakes", view);
    }

    @Test
    void listarProdutosCliente_comCarrinhoNulo_deveCriarCarrinhoNovo() {
        when(produtoService.listar()).thenReturn(List.of());
        when(session.getAttribute("carrinho")).thenReturn(null);

        String view = produtoController.listarProdutosCliente(model, session);

        verify(session).setAttribute(eq("carrinho"), any(Carrinho.class));
        verify(model, atLeastOnce()).addAttribute(eq("produtos"), anyList());
        verify(model).addAttribute(eq("carrinho"), any(Carrinho.class));
        verify(model).addAttribute(eq("valorTotal"), any());
        assertEquals("pages/comprar-cupcakes", view);
    }

    @Test
    void listarProdutosCliente_comCarrinhoExistente() {
        Carrinho carrinho = new Carrinho();
        when(produtoService.listar()).thenReturn(List.of());
        when(session.getAttribute("carrinho")).thenReturn(carrinho);

        String view = produtoController.listarProdutosCliente(model, session);

        verify(model).addAttribute("carrinho", carrinho);
        verify(model).addAttribute("valorTotal", carrinho.calcularValorTotal());
        assertEquals("pages/comprar-cupcakes", view);
    }

    @Test
    void deletarProduto_deveChamarServiceECaminharPraLista() {
        String view = produtoController.deletarProduto(1L);

        verify(produtoService).deletar(1L);
        assertEquals("redirect:/cupcakes/all", view);
    }

    @Test
    void mostrarFormularioEditarCadastro_deveBuscarProdutoEPopularDTO() {
        Produto produto = new Produto();
        produto.setId(42L);
        when(produtoService.buscarPorId(42L)).thenReturn(produto);

        String view = produtoController.mostrarFormularioEditarCadastro(42L, model);

        verify(model).addAttribute(eq("produto"), any(ProdutoDTO.class));
        assertEquals("pages/editar-cupcake", view);
    }

    @Test
    void editarProduto_deveChamarServiceEditar() {
        ProdutoDTO dto = new ProdutoDTO();
        String view = produtoController.editarProduto(dto);

        verify(produtoService).editar(dto);
        assertEquals("redirect:/cupcakes/all", view);
    }
}
