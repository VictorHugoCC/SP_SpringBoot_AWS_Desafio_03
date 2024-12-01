package org.service.cliente.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "O nome não pode estar vazio")
    private String nome;

    @NotEmpty(message = "O email não pode estar vazio")
    @Email(message = "O email deve ser válido")
    private String email;

    private String telefone;

    private String endereco;

    @Transient
    private List<Object> pedidos = new ArrayList<>();
}
