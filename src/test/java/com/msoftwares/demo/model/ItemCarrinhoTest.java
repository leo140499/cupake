package com.msoftwares.demo.model;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemCarrinhoTest {

    @Test
    void deveCriarItemCarrinhoCorretamente() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Cupcake de Chocolate");
        produto.setPreco(new BigDecimal("9.90"));

        int quantidade = 3;
        ItemCarrinho item = new ItemCarrinho(produto, quantidade);

        assertEquals(produto, item.getProduto());
        assertEquals(quantidade, item.getQuantidade());
    }

    @Test
    void devePermitirAlterarProdutoEQuantidade() {
        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Cupcake A");

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Cupcake B");

        ItemCarrinho item = new ItemCarrinho(produto1, 2);
        item.setProduto(produto2);
        item.setQuantidade(5);

        assertEquals(produto2, item.getProduto());
        assertEquals(5, item.getQuantidade());
    }

    @Test
    void deveSerSerializable() {
        assertTrue(Serializable.class.isAssignableFrom(ItemCarrinho.class));
    }
}
