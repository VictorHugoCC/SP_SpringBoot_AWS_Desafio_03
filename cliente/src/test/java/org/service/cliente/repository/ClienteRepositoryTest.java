package org.service.cliente.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.service.cliente.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Deve salvar um cliente com sucesso")
    public void deveSalvarClienteComSucesso() {
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao.silva@example.com");
        cliente.setTelefone("123456789");

        Cliente clienteSalvo = clienteRepository.save(cliente);

        assertThat(clienteSalvo).isNotNull();
        assertThat(clienteSalvo.getId()).isNotNull();
        assertThat(clienteSalvo.getNome()).isEqualTo("João Silva");
        assertThat(clienteSalvo.getEmail()).isEqualTo("joao.silva@example.com");
    }

    @Test
    @DisplayName("Deve buscar um cliente por ID com sucesso")
    public void deveBuscarClientePorIdComSucesso() {
        Cliente cliente = new Cliente();
        cliente.setNome("Maria Souza");
        cliente.setEmail("maria.souza@example.com");
        cliente.setTelefone("987654321");

        Cliente clienteSalvo = clienteRepository.save(cliente);

        Optional<Cliente> clienteEncontrado = clienteRepository.findById(clienteSalvo.getId());

        assertThat(clienteEncontrado).isPresent();
        assertThat(clienteEncontrado.get().getNome()).isEqualTo("Maria Souza");
        assertThat(clienteEncontrado.get().getEmail()).isEqualTo("maria.souza@example.com");
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar cliente com ID inexistente")
    public void deveRetornarVazioAoBuscarClienteComIdInexistente() {
        Optional<Cliente> clienteEncontrado = clienteRepository.findById(999L);

        assertThat(clienteEncontrado).isEmpty();
    }

    @Test
    @DisplayName("Deve deletar um cliente com sucesso")
    public void deveDeletarClienteComSucesso() {
        Cliente cliente = new Cliente();
        cliente.setNome("Carlos Santos");
        cliente.setEmail("carlos.santos@example.com");
        cliente.setTelefone("321654987");

        Cliente clienteSalvo = clienteRepository.save(cliente);

        clienteRepository.deleteById(clienteSalvo.getId());
        Optional<Cliente> clienteDeletado = clienteRepository.findById(clienteSalvo.getId());

        assertThat(clienteDeletado).isEmpty();
    }

    @Test
    @DisplayName("Deve verificar se um cliente existe por ID")
    public void deveVerificarSeClienteExistePorId() {
        Cliente cliente = new Cliente();
        cliente.setNome("Ana Paula");
        cliente.setEmail("ana.paula@example.com");
        cliente.setTelefone("456789123");

        Cliente clienteSalvo = clienteRepository.save(cliente);

        boolean existe = clienteRepository.existsById(clienteSalvo.getId());

        assertThat(existe).isTrue();
    }
}
