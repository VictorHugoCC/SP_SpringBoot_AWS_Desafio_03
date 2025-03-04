package org.service.estoque.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProdutoCreateDTO {
    private String nome;
    private String descricao;
    private int quantidade;

    public ProdutoCreateDTO() {
    }

    public ProdutoCreateDTO(String nome, String descricao, int quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

}
