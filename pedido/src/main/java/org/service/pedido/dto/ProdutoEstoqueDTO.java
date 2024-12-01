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

    public ProdutoEstoqueDTO(Long id, String nome, int quantidade, double preco) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
    }
}
