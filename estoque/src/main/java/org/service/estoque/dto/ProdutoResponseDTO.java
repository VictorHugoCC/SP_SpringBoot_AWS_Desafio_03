package org.service.estoque.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private int quantidade;
}

