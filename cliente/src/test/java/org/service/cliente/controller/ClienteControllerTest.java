package org.service.cliente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.service.cliente.dto.ClienteResponseDTO;
import org.service.cliente.model.Cliente;
import org.service.cliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    @BeforeEach
    void configurar() {
        Mockito.reset(clienteService);
    }

    @Test
    void deveRetornarListaVaziaDeClientes() throws Exception {
        when(clienteService.findAllClientesComPedidos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deveRetornarListaDeClientesComDados() throws Exception {
        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO();
        clienteResponseDTO.setId(1L);
        clienteResponseDTO.setNome("joaozinho");
        clienteResponseDTO.setEmail("joaozinho.aa@example.com");
        clienteResponseDTO.setPedidos(Collections.emptyList());

        when(clienteService.findAllClientesComPedidos()).thenReturn(Collections.singletonList(clienteResponseDTO));

        mockMvc.perform(get("/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("joaozinho"))
                .andExpect(jsonPath("$[0].email").value("joaozinho.aa@example.com"));
    }

    @Test
    void deveCriarClienteComSucesso() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Novo");
        cliente.setEmail("cliente@teste.com");

        when(clienteService.createCliente(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Cliente Novo"))
                .andExpect(jsonPath("$.email").value("cliente@teste.com"));
    }


    @Test
    void deveAtualizarClienteComSucesso() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente Atualizado");
        cliente.setEmail("atualizado@teste.com");

        when(clienteService.updateCliente(anyLong(), any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Cliente Atualizado"))
                .andExpect(jsonPath("$.email").value("atualizado@teste.com"));
    }

    @Test
    void deveRetornarNotFoundAoAtualizarClienteInexistente() throws Exception {
        when(clienteService.updateCliente(anyLong(), any(Cliente.class))).thenThrow(new IllegalArgumentException("Cliente não encontrado"));

        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveExcluirClienteComSucesso() throws Exception {
        mockMvc.perform(delete("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoExcluirClienteInexistente() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Cliente não encontrado"))
                .when(clienteService).deleteCliente(1L);

        mockMvc.perform(delete("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}


