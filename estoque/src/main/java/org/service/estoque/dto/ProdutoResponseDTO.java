package org.service.estoque.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private int quantidade;

    public ProdutoResponseDTO(Long id, String nome, String descricao, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

}
