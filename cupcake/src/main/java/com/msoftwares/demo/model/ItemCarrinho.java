package com.msoftwares.demo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ItemCarrinho implements Serializable {
    private static final long serialVersionUID = 1L;

    private Produto produto;
    private int quantidade;

    public ItemCarrinho(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }
}