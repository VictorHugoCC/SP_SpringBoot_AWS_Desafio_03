package org.service.cliente.service;

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

    public ClienteService(ClienteRepository clienteRepository, PedidoClient pedidoClient) {
        this.clienteRepository = clienteRepository;
        this.pedidoClient = pedidoClient;
    }

    public Object getHistoricoPedidos(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new IllegalArgumentException("Cliente com ID " + clienteId + " não encontrado.");
        }
        return pedidoClient.getPedidosByClienteId(clienteId);
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void deleteById(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente com ID " + id + " não encontrado.");
        }
        clienteRepository.deleteById(id);
    }
}
