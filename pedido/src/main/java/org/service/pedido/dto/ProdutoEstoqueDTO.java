package org.service.pedido.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoEstoqueDTO {
    private Long id;
    private String nome;
    private int quantidade;
    private double preco;
}
