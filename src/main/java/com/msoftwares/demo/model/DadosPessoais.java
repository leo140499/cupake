package com.msoftwares.demo.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class DadosPessoais implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String email;
    private String senha;
    private String documento;

    @Embedded
    private Endereco endereco;
}
