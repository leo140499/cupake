package com.msoftwares.demo.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Carrinho implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ItemCarrinho> itens = new ArrayList<>();

    public void adicionarItem(Produto produto, int quantidade) {
        // Verifica se o produto já está no carrinho
        for (ItemCarrinho item : itens) {
            if (item.getProduto().getId().equals(produto.getId())) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                return;
            }
        }
        // Se não estiver, adiciona novo item
        itens.add(new ItemCarrinho(produto, quantidade));
    }

    public BigDecimal calcularValorTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemCarrinho item : itens) {
            BigDecimal subtotal = item.getProduto().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
            total = total.add(subtotal);
        }
        return total;
    }

    public void removerProduto(Long idProduto) {
        itens.removeIf(item -> item.getProduto().getId().equals(idProduto));
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public void setItens(List<ItemCarrinho> itens) {
        this.itens = itens;
    }
}
