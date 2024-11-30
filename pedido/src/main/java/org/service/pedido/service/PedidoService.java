package org.service.pedido.service;

import org.service.pedido.dto.PedidoRequestDTO;
import org.service.pedido.dto.PedidoResponseDTO;
import org.service.pedido.dto.ProdutoEstoqueDTO;
import org.service.pedido.exception.*;
import org.service.pedido.feign.ClienteClient;
import org.service.pedido.feign.EstoqueClient;
import org.service.pedido.model.Pedido;
import org.service.pedido.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private EstoqueClient estoqueClient;

    @Autowired
    private ClienteClient clienteClient;

    public PedidoService(PedidoRepository pedidoRepository, EstoqueClient estoqueClient) {
        this.pedidoRepository = pedidoRepository;
        this.estoqueClient = estoqueClient;
    }

    public PedidoResponseDTO criarPedido(PedidoRequestDTO pedidoRequestDTO) {
        logger.info("Iniciando a criação do pedido para o cliente ID: {}", pedidoRequestDTO.getCliente());

        if (pedidoRequestDTO.getProdutos() == null || pedidoRequestDTO.getProdutos().isEmpty()) {
            logger.error("A lista de produtos está vazia para o pedido do cliente ID: {}", pedidoRequestDTO.getCliente());
            throw new IllegalArgumentException("A lista de produtos não pode estar vazia.");
        }

        try {
            ResponseEntity<Object> clienteResponse = clienteClient.getClienteById(pedidoRequestDTO.getCliente());
            if (clienteResponse.getBody() == null) {
                throw new ClienteNotFoundException("Cliente com ID " + pedidoRequestDTO.getCliente() + " não encontrado.");
            }
        } catch (ClienteNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ClienteIntegrationException("Falha ao conectar ao serviço de cliente.", e);
        }

        logger.info("Cliente encontrado. Validando estoque para os produtos do pedido.");

        pedidoRequestDTO.getProdutos().forEach((produtoId, quantidade) -> {
            try {
                ProdutoEstoqueDTO produtoEstoque = estoqueClient.getProdutoById(produtoId);

                if (produtoEstoque.getQuantidade() < quantidade) {
                    logger.error("Estoque insuficiente para Produto ID: {}. Quantidade disponível: {}", produtoId, produtoEstoque.getQuantidade());
                    throw new EstoqueInsuficienteException("Estoque insuficiente para Produto ID: " + produtoId);
                }

                estoqueClient.atualizarQuantidade(produtoId, quantidade);
                logger.info("Estoque atualizado com sucesso para Produto ID: {}. Quantidade restante: {}", produtoId, produtoEstoque.getQuantidade() - quantidade);
            } catch (EstoqueInsuficienteException e) {
                throw e;
            } catch (Exception e) {
                logger.error("Erro ao validar ou atualizar estoque para Produto ID: {}. Detalhes: {}", produtoId, e.getMessage());
                ProdutoEstoqueDTO produtoEstoque = estoqueClient.getProdutoById(produtoId);
                logger.info("Quantidade total no estoque para Produto ID {}: {}", produtoId, produtoEstoque.getQuantidade());
                throw new EstoqueInsuficienteException("Erro ao processar Produto ID: " + produtoId + ". Estoque atual: " + produtoEstoque.getQuantidade());
            }
        });

        Pedido pedido = new Pedido();
        pedido.setCliente(pedidoRequestDTO.getCliente().toString());
        pedido.setProdutos(pedidoRequestDTO.getProdutos());
        pedido.setStatus("CONFIRMADO");

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        logger.info("Pedido criado com sucesso. ID do Pedido: {}, Cliente ID: {}", pedidoSalvo.getId(), pedidoSalvo.getCliente());

        return new PedidoResponseDTO(
                pedidoSalvo.getId(),
                pedidoSalvo.getCliente(),
                pedidoSalvo.getProdutos(),
                pedidoSalvo.getStatus()
        );
    }
    
    public List<PedidoResponseDTO> findAll() {
        logger.info("Buscando todos os pedidos no sistema...");
        List<PedidoResponseDTO> pedidos = pedidoRepository.findAll().stream()
                .map(pedido -> new PedidoResponseDTO(
                        pedido.getId(),
                        pedido.getCliente(),
                        pedido.getProdutos(),
                        pedido.getStatus()
                ))
                .collect(Collectors.toList());
        logger.info("Total de pedidos encontrados: {}", pedidos.size());
        return pedidos;
    }

    public PedidoResponseDTO buscarPedidoPorId(Long id) {
        logger.info("Buscando pedido pelo ID: {}", id);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido com ID " + id + " não encontrado."));

        logger.info("Pedido encontrado: ID: {}", pedido.getId());
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getCliente(),
                pedido.getProdutos(),
                pedido.getStatus()
        );
    }

    public PedidoResponseDTO atualizarStatus(Long id, String novoStatus) {
        logger.info("Atualizando status do pedido ID: {} para: {}", id, novoStatus);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido com ID " + id + " não encontrado."));

        try {
            pedido.setStatus(novoStatus);
            Pedido pedidoAtualizado = pedidoRepository.save(pedido);
            logger.info("Status do pedido ID: {} atualizado para: {}", pedidoAtualizado.getId(), novoStatus);

            return new PedidoResponseDTO(
                    pedidoAtualizado.getId(),
                    pedidoAtualizado.getCliente(),
                    pedidoAtualizado.getProdutos(),
                    pedidoAtualizado.getStatus()
            );
        } catch (Exception e) {
            throw new PedidoStatusUpdateException("Erro ao atualizar o status do pedido ID: " + id);
        }
    }


    public void deletarPedido(Long id) {
        logger.info("Deletando pedido ID: {}", id);
        if (!pedidoRepository.existsById(id)) {
            throw new PedidoDeletionException("Pedido com ID " + id + " não encontrado.");
        }

        try {
            pedidoRepository.deleteById(id);
            logger.info("Pedido ID: {} deletado com sucesso!", id);
        } catch (Exception e) {
            throw new PedidoDeletionException("Erro ao deletar o pedido ID: " + id);
        }
    }

    public void deletarTodosOsPedidos() {
        logger.warn("Deletando todos os pedidos do sistema...");
        try {
            pedidoRepository.deleteAll();
            logger.info("Todos os pedidos foram deletados com sucesso.");
        } catch (Exception e) {
            throw new PedidoDeletionException("Erro ao deletar todos os pedidos.");
        }
    }


    public List<PedidoResponseDTO> findByClienteId(Long clienteId) {
        logger.info("Buscando pedidos para o cliente ID: {}", clienteId);

        List<Pedido> pedidos = pedidoRepository.findByCliente(clienteId.toString());

        return pedidos.stream()
                .map(pedido -> new PedidoResponseDTO(
                        pedido.getId(),
                        pedido.getCliente(),
                        pedido.getProdutos(),
                        pedido.getStatus()
                ))
                .collect(Collectors.toList());
    }
}
