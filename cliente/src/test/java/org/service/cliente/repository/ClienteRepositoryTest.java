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
    @DisplayName("Deve retornar vazio ao buscar cliente com ID inexistente")
    public void deveRetornarVazioAoBuscarClienteComIdInexistente() {
        Optional<Cliente> clienteEncontrado = clienteRepository.findById(999L);

        assertThat(clienteEncontrado).isEmpty();
    }


}
