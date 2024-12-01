package org.service.cliente.dto;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClienteResponseDTOTest {

    @Test
    void deveTestarGettersESetters() {
        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO();

        Long id = 1L;
        String nome = "Victor Hugo";
        String email = "victor.hugo@example.com";
        String telefone = "123-456-7890";
        List<Object> pedidos = new ArrayList<>();
        pedidos.add("Pedido1");
        pedidos.add("Pedido2");

        clienteResponseDTO.setId(id);
        clienteResponseDTO.setNome(nome);
        clienteResponseDTO.setEmail(email);
        clienteResponseDTO.setTelefone(telefone);
        clienteResponseDTO.setPedidos(pedidos);

        assertEquals(id, clienteResponseDTO.getId());
        assertEquals(nome, clienteResponseDTO.getNome());
        assertEquals(email, clienteResponseDTO.getEmail());
        assertEquals(telefone, clienteResponseDTO.getTelefone());
        assertEquals(pedidos, clienteResponseDTO.getPedidos());
    }

    @Test
    void deveTestarConstrutorPadrao() {
        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO();

        assertNull(clienteResponseDTO.getId());
        assertNull(clienteResponseDTO.getNome());
        assertNull(clienteResponseDTO.getEmail());
        assertNull(clienteResponseDTO.getTelefone());
        assertNull(clienteResponseDTO.getPedidos());
    }

    @Test
    void deveModificarListaDePedidos() {
        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO();
        List<Object> pedidos = new ArrayList<>();
        clienteResponseDTO.setPedidos(pedidos);

        clienteResponseDTO.getPedidos().add("Novo Pedido");

        assertEquals(1, clienteResponseDTO.getPedidos().size());
        assertEquals("Novo Pedido", clienteResponseDTO.getPedidos().get(0));
    }
}
