package com.msoftwares.demo.dto;

import lombok.Data;

@Data
public class ClienteDTO {

    private String nome;
    private String email;
    private String cpf;
    private String senha;
    private EnderecoDTO enderecoDTO;

}
