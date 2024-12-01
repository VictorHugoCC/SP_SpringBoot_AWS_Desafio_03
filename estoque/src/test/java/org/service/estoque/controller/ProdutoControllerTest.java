package org.service.estoque.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.service.estoque.dto.ProdutoCreateDTO;
import org.service.estoque.dto.ProdutoResponseDTO;
import org.service.estoque.model.Produto;
import org.service.estoque.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProdutoService produtoService;

    @BeforeEach
    void configurar() {
        Mockito.reset(produtoService);
    }

    @Test
    void deveRetornarListaVaziaDeProdutos() throws Exception {
        when(produtoService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/produtos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deveRetornarListaDeProdutosComDados() throws Exception {
        // Configurando um objeto Produto com valores válidos
        Produto produto = new Produto(1L, "Produto Teste", "Descrição Teste", 100);

        // Configurando o mock para retornar a lista de produtos
        when(produtoService.findAll()).thenReturn(Collections.singletonList(produto));

        // Executando o teste
        mockMvc.perform(get("/produtos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Produto Teste"))
                .andExpect(jsonPath("$[0].descricao").value("Descrição Teste"))
                .andExpect(jsonPath("$[0].quantidade").value(100));
    }


    @Test
    void deveCriarProdutoComSucesso() throws Exception {
        // DTO de criação de produto
        ProdutoCreateDTO produtoDTO = new ProdutoCreateDTO();
        produtoDTO.setNome("Produto Novo");
        produtoDTO.setDescricao("Descrição Nova");
        produtoDTO.setQuantidade(200);

        // Objeto Produto esperado
        Produto produtoCriado = new Produto(1L, "Produto Novo", "Descrição Nova", 200);

        // Configurando o mock para salvar o produto
        when(produtoService.save(any(Produto.class))).thenReturn(produtoCriado);

        // Executando o teste
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Produto Novo"))
                .andExpect(jsonPath("$.descricao").value("Descrição Nova"))
                .andExpect(jsonPath("$.quantidade").value(200));
    }


    @Test
    void deveAtualizarQuantidadeDoProdutoComSucesso() throws Exception {
        // Objeto Produto atualizado esperado
        Produto produtoAtualizado = new Produto(1L, "Produto Atualizado", "Descrição Atualizada", 150);

        // Configurando o mock para atualizar a quantidade do produto
        when(produtoService.atualizarQuantidade(anyLong(), eq(150))).thenReturn(produtoAtualizado);

        // Executando o teste
        mockMvc.perform(put("/produtos/1/quantidade")
                        .param("quantidade", "150")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Produto Atualizado"))
                .andExpect(jsonPath("$.descricao").value("Descrição Atualizada"))
                .andExpect(jsonPath("$.quantidade").value(150));
    }



    @Test
    void deveDeletarProdutoComSucesso() throws Exception {
        when(produtoService.findById(1L)).thenReturn(Optional.of(new Produto()));

        mockMvc.perform(delete("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoDeletarProdutoInexistente() throws Exception {
        when(produtoService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

