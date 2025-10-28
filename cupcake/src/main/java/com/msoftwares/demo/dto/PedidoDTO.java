package com.msoftwares.demo.dto;

import com.msoftwares.demo.model.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoDTO {

    private ClienteDTO clienteDTO;

    private List<ProdutoDTO> produtos;

    private LocalDateTime dataHoraPedido;

    private StatusPedido status;

    private FormaPagamento pagamento;
}
