package org.service.cliente.service;

import org.service.cliente.dto.ClienteResponseDTO;
import org.service.cliente.exception.ClienteNotFoundException;
import org.service.cliente.feign.PedidoClient;
import org.service.cliente.model.Cliente;
import org.service.cliente.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private static Logger logger = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoClient pedidoClient;

    public ClienteResponseDTO findClienteComPedidos(Long clienteId) {
        logger.info("Buscando cliente com ID {} e seus pedidos", clienteId);

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> {
                    logger.warn("Cliente com ID {} não encontrado.", clienteId);
                    return new ClienteNotFoundException("Cliente não encontrado com ID " + clienteId);
                });

        // Busque os pedidos do cliente
        List<Object> pedidos;
        try {
            pedidos = pedidoClient.getPedidosByClienteId(clienteId); // Use o Feign para buscar os pedidos
        } catch (Exception e) {
            logger.error("Erro ao buscar pedidos para o cliente ID {}: {}", clienteId, e.getMessage());
            pedidos = Collections.emptyList();
        }

        // Converta o cliente e seus pedidos para ClienteResponseDTO
        ClienteResponseDTO clienteResponse = new ClienteResponseDTO();
        clienteResponse.setId(cliente.getId());
        clienteResponse.setNome(cliente.getNome());
        clienteResponse.setEmail(cliente.getEmail());
        clienteResponse.setTelefone(cliente.getTelefone());
        clienteResponse.setPedidos(pedidos);

        return clienteResponse;
    }


    public List<ClienteResponseDTO> findAllClientesComPedidos() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(cliente -> {
                    ClienteResponseDTO clienteResponse = new ClienteResponseDTO();
                    clienteResponse.setId(cliente.getId());
                    clienteResponse.setNome(cliente.getNome());
                    clienteResponse.setEmail(cliente.getEmail());
                    clienteResponse.setTelefone(cliente.getTelefone());

                    // Busque pedidos relacionados
                    try {
                        List<Object> pedidos = pedidoClient.getPedidosByClienteId(cliente.getId());
                        clienteResponse.setPedidos(pedidos);
                    } catch (Exception e) {
                        logger.warn("Erro ao buscar pedidos para cliente {}: {}", cliente.getId(), e.getMessage());
                        clienteResponse.setPedidos(Collections.emptyList());
                    }

                    return clienteResponse;
                })
                .collect(Collectors.toList());
    }


    public Optional<Cliente> findClienteById(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        cliente.ifPresent(c -> {
            try {
                List<Object> pedidos = pedidoClient.getPedidosByClienteId(c.getId());
                c.setPedidos(pedidos);
            } catch (Exception e) {
                c.setPedidos(Collections.emptyList());
            }
        });
        return cliente;
    }

    public Cliente createCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente updateCliente(Long id, Cliente updatedCliente) {
        return clienteRepository.findById(id)
                .map(existingCliente -> {
                    existingCliente.setNome(updatedCliente.getNome());
                    existingCliente.setEmail(updatedCliente.getEmail());
                    existingCliente.setTelefone(updatedCliente.getTelefone());
                    existingCliente.setEndereco(updatedCliente.getEndereco());
                    return clienteRepository.save(existingCliente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Cliente com ID " + id + " não encontrado."));
    }

    public void deleteCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente com ID " + id + " não encontrado.");
        }
        clienteRepository.deleteById(id);
    }
}
