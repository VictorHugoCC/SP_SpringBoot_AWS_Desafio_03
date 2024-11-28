package org.service.cliente.service;

import org.service.cliente.exception.ClienteNotFoundException;
import org.service.cliente.feign.PedidoClient;
import org.service.cliente.model.Cliente;
import org.service.cliente.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoClient pedidoClient;

    public Object getHistoricoPedidos(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ClienteNotFoundException("Cliente com ID " + clienteId + " não encontrado.");
        }
        try {
            return pedidoClient.getPedidosByClienteId(clienteId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter histórico de pedidos: " + e.getMessage(), e);
        }
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente save(Cliente cliente) {
        validateCliente(cliente);
        return clienteRepository.save(cliente);
    }

    public void deleteById(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNotFoundException("Cliente com ID " + id + " não encontrado.");
        }
        clienteRepository.deleteById(id);
    }

    private void validateCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
            throw new IllegalArgumentException("O email do cliente é obrigatório.");
        }
    }
}
