package org.service.cliente.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.service.cliente.dto.ClienteResponseDTO;
import org.service.cliente.exception.ClienteNotFoundException;
import org.service.cliente.feign.PedidoClient;
import org.service.cliente.model.Cliente;
import org.service.cliente.repository.ClienteRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PedidoClient pedidoClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveBuscarTodosClientesComPedidos() {
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNome("Cliente 1");

        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNome("Cliente 2");

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));
        when(pedidoClient.getPedidosByClienteId(1L)).thenReturn(Collections.singletonList(new Object()));
        when(pedidoClient.getPedidosByClienteId(2L)).thenReturn(Collections.emptyList());

        List<ClienteResponseDTO> clientesComPedidos = clienteService.findAllClientesComPedidos();

        assertEquals(2, clientesComPedidos.size());
        assertEquals("Cliente 1", clientesComPedidos.get(0).getNome());
        assertEquals(1, clientesComPedidos.get(0).getPedidos().size());
        assertEquals("Cliente 2", clientesComPedidos.get(1).getNome());
        assertEquals(0, clientesComPedidos.get(1).getPedidos().size());

        verify(clienteRepository, times(1)).findAll();
        verify(pedidoClient, times(2)).getPedidosByClienteId(anyLong());
    }

    @Test
    void deveBuscarClientePorId() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Cliente 1");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(pedidoClient.getPedidosByClienteId(1L)).thenReturn(Collections.singletonList(new Object()));

        Optional<Cliente> result = clienteService.findClienteById(1L);

        assertTrue(result.isPresent());
        assertEquals("Cliente 1", result.get().getNome());
        assertEquals(1, result.get().getPedidos().size());

        verify(clienteRepository, times(1)).findById(1L);
        verify(pedidoClient, times(1)).getPedidosByClienteId(1L);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        ClienteNotFoundException exception = assertThrows(
                ClienteNotFoundException.class,
                () -> clienteService.updateCliente(1L, new Cliente())
        );

        assertEquals("Cliente com ID 1 não encontrado.", exception.getMessage());

        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void deveCriarClienteComSucesso() {
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Novo");

        Cliente savedCliente = new Cliente();
        savedCliente.setId(1L);
        savedCliente.setNome("Cliente Novo");

        when(clienteRepository.save(cliente)).thenReturn(savedCliente);

        Cliente result = clienteService.createCliente(cliente);

        assertNotNull(result.getId());
        assertEquals("Cliente Novo", result.getNome());

        verify(clienteRepository, times(1)).save(cliente);
    }
}
