package com.msoftwares.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarrinhoTest {

    private Carrinho carrinho;
    private Produto cupcakeBaunilha;
    private Produto cupcakeChocolate;

    @BeforeEach
    void setUp() {
        carrinho = new Carrinho();

        cupcakeBaunilha = new Produto();
        cupcakeBaunilha.setId(1L);
        cupcakeBaunilha.setNome("Cupcake de Baunilha");
        cupcakeBaunilha.setPreco(new BigDecimal("5.00"));

        cupcakeChocolate = new Produto();
        cupcakeChocolate.setId(2L);
        cupcakeChocolate.setNome("Cupcake de Chocolate");
        cupcakeChocolate.setPreco(new BigDecimal("7.50"));
    }

    @Test
    void deveAdicionarItemAoCarrinho() {
        carrinho.adicionarItem(cupcakeBaunilha, 2);

        List<ItemCarrinho> itens = carrinho.getItens();
        assertEquals(1, itens.size());
        assertEquals(cupcakeBaunilha, itens.get(0).getProduto());
        assertEquals(2, itens.get(0).getQuantidade());
    }

    @Test
    void deveSomarQuantidadeSeProdutoJaExistir() {
        carrinho.adicionarItem(cupcakeBaunilha, 1);
        carrinho.adicionarItem(cupcakeBaunilha, 3);

        List<ItemCarrinho> itens = carrinho.getItens();
        assertEquals(1, itens.size());
        assertEquals(4, itens.get(0).getQuantidade());
    }

    @Test
    void deveCalcularValorTotalCorretamente() {
        carrinho.adicionarItem(cupcakeBaunilha, 2); // 2 x 5.00 = 10.00
        carrinho.adicionarItem(cupcakeChocolate, 1); // 1 x 7.50 = 7.50

        BigDecimal total = carrinho.calcularValorTotal();
        assertEquals(new BigDecimal("17.50"), total);
    }

    @Test
    void deveRemoverProdutoDoCarrinho() {
        carrinho.adicionarItem(cupcakeBaunilha, 2);
        carrinho.adicionarItem(cupcakeChocolate, 1);

        carrinho.removerProduto(cupcakeBaunilha.getId());

        List<ItemCarrinho> itens = carrinho.getItens();
        assertEquals(1, itens.size());
        assertEquals(cupcakeChocolate, itens.get(0).getProduto());
    }

    @Test
    void carrinhoInicialmenteVazio() {
        assertTrue(carrinho.getItens().isEmpty());
    }
}
