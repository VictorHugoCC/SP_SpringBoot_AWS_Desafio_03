package org.service.cliente.service;

import org.service.cliente.dto.ClienteResponseDTO;
import org.service.cliente.exception.ClienteNotFoundException;
import org.service.cliente.exception.ClienteValidationException;
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

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoClient pedidoClient;

    public List<ClienteResponseDTO> findAllClientesComPedidos() {
        logger.info("Buscando todos os clientes com seus pedidos...");
        List<Cliente> clientes = clienteRepository.findAll();

        return clientes.stream()
                .map(cliente -> {
                    ClienteResponseDTO clienteResponse = new ClienteResponseDTO();
                    clienteResponse.setId(cliente.getId());
                    clienteResponse.setNome(cliente.getNome());
                    clienteResponse.setEmail(cliente.getEmail());
                    clienteResponse.setTelefone(cliente.getTelefone());

                    try {
                        logger.info("Buscando pedidos para o cliente ID: {}", cliente.getId());
                        List<Object> pedidos = pedidoClient.getPedidosByClienteId(cliente.getId());
                        clienteResponse.setPedidos(pedidos);
                        logger.info("Pedidos encontrados para cliente ID {}: {}", cliente.getId(), pedidos.size());
                    } catch (Exception e) {
                        logger.warn("Erro ao buscar pedidos para cliente ID {}: {}", cliente.getId(), e.getMessage());
                        clienteResponse.setPedidos(Collections.emptyList());
                    }

                    return clienteResponse;
                })
                .collect(Collectors.toList());
    }

    public Optional<Cliente> findClienteById(Long id) {
        logger.info("Buscando cliente pelo ID: {}", id);
        Optional<Cliente> cliente = clienteRepository.findById(id);

        cliente.ifPresent(c -> {
            try {
                logger.info("Buscando pedidos para cliente ID: {}", c.getId());
                List<Object> pedidos = pedidoClient.getPedidosByClienteId(c.getId());
                c.setPedidos(pedidos);
                logger.info("Pedidos encontrados para cliente ID {}: {}", c.getId(), pedidos.size());
            } catch (Exception e) {
                logger.warn("Erro ao buscar pedidos para cliente ID {}: {}", c.getId(), e.getMessage());
                c.setPedidos(Collections.emptyList());
            }
        });

        return cliente;
    }

    public Cliente createCliente(Cliente cliente) {
        logger.info("Criando novo cliente: {}", cliente.getNome());
        Cliente clienteSalvo = clienteRepository.save(cliente);
        logger.info("Cliente criado com sucesso. ID: {}", clienteSalvo.getId());
        return clienteSalvo;
    }

    public Cliente updateCliente(Long id, Cliente updatedCliente) {
        logger.info("Atualizando cliente com ID: {}", id);

        if (updatedCliente.getNome() == null || updatedCliente.getEmail() == null) {
            logger.error("Dados inválidos fornecidos para o cliente com ID: {}", id);
            throw new ClienteValidationException("Dados inválidos fornecidos para o cliente.");
        }

        return clienteRepository.findById(id)
                .map(existingCliente -> {
                    existingCliente.setNome(updatedCliente.getNome());
                    existingCliente.setEmail(updatedCliente.getEmail());
                    existingCliente.setTelefone(updatedCliente.getTelefone());
                    existingCliente.setEndereco(updatedCliente.getEndereco());
                    Cliente clienteAtualizado = clienteRepository.save(existingCliente);
                    logger.info("Cliente com ID {} atualizado com sucesso.", id);
                    return clienteAtualizado;
                })
                .orElseThrow(() -> {
                    logger.error("Cliente com ID {} não encontrado para atualização.", id);
                    return new ClienteNotFoundException("Cliente com ID " + id + " não encontrado.");
                });
    }


    public void deleteCliente(Long id) {
        logger.info("Deletando cliente com ID: {}", id);

        if (!clienteRepository.existsById(id)) {
            logger.error("Cliente com ID {} não encontrado para exclusão.", id);
            throw new IllegalArgumentException("Cliente com ID " + id + " não encontrado.");
        }

        clienteRepository.deleteById(id);
        logger.info("Cliente com ID {} deletado com sucesso.", id);
    }
}
