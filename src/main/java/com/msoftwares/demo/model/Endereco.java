package com.msoftwares.demo.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class Endereco implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rua;
    private String bairro;
    private String cidade;
    private String complemento;

}