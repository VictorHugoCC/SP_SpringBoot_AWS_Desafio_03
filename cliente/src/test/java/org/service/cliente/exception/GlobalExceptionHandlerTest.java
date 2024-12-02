package org.service.cliente.exception;

import org.junit.jupiter.api.Test;
import org.service.cliente.controller.ClienteController;
import org.service.cliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Test
    void handleClienteNotFoundException() throws Exception {
        when(clienteService.findClienteById(999L)).thenThrow(new ClienteNotFoundException("Cliente não encontrado!"));

        mockMvc.perform(get("/clientes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Cliente não encontrado!"));
    }



    @Test
    void handleGenericException() throws Exception {
        when(clienteService.findClienteById(1L)).thenThrow(new RuntimeException("Erro genérico"));

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("Erro interno no servidor: Erro genérico"));
    }
}
