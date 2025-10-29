package com.msoftwares.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.aot.generate.GenerationContext;

import java.io.Serializable;

@Data
@Entity
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private DadosPessoais dadosPessoais;

}
