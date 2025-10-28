package com.msoftwares.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoDTO {

    Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
}
