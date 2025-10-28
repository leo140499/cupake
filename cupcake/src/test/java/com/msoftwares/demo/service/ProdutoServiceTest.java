package com.msoftwares.demo.service;

import com.msoftwares.demo.dto.ProdutoDTO;
import com.msoftwares.demo.model.Produto;
import com.msoftwares.demo.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrar_deveSalvarProduto() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Cupcake de Chocolate");
        dto.setPreco(new BigDecimal("9.90"));
        dto.setDescricao("Delicioso cupcake");

        produtoService.cadastrar(dto);

        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoRepository).save(captor.capture());

        Produto salvo = captor.getValue();
        assertEquals("Cupcake de Chocolate", salvo.getNome());
        assertEquals(new BigDecimal("9.90"), salvo.getPreco());
        assertEquals("Delicioso cupcake", salvo.getDescricao());
    }

    @Test
    void listar_deveRetornarListaDeProdutos() {
        Produto p1 = new Produto();
        Produto p2 = new Produto();

        when(produtoRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Produto> produtos = produtoService.listar();

        assertEquals(2, produtos.size());
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    void deletar_deveRemoverProdutoExistente() {
        Produto produto = new Produto();
        produto.setId(1L);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        produtoService.deletar(1L);

        verify(produtoRepository).delete(produto);
    }

    @Test
    void editar_deveAtualizarProduto() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(1L);
        dto.setNome("Cupcake Atualizado");
        dto.setDescricao("Descrição atualizada");
        dto.setPreco(new BigDecimal("12.50"));

        Produto existente = new Produto();
        existente.setId(1L);
        existente.setNome("Antigo");
        existente.setDescricao("Velha");
        existente.setPreco(new BigDecimal("5.00"));

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(existente));

        produtoService.editar(dto);

        assertEquals("Cupcake Atualizado", existente.getNome());
        assertEquals("Descrição atualizada", existente.getDescricao());
        assertEquals(new BigDecimal("12.50"), existente.getPreco());
        verify(produtoRepository).save(existente);
    }

    @Test
    void buscarPorId_deveRetornarProduto() {
        Produto produto = new Produto();
        produto.setId(1L);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Produto resultado = produtoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscarPorId_deveLancarExcecaoQuandoNaoEncontrado() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> produtoService.buscarPorId(1L));
    }
}
