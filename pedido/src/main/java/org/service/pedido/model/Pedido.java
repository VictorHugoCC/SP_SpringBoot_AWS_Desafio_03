package org.service.pedido.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cliente;

    @ElementCollection
    @CollectionTable(name = "pedido_produto", joinColumns = @JoinColumn(name = "pedido_id"))
    @MapKeyColumn(name = "produto_id")
    @Column(name = "quantidade")
    private Map<Long, Integer> produtos;

    @Column(nullable = false)
    private String status;

    public Pedido() {
    }

    public Pedido(Long id, String cliente, Map<Long, Integer> produtos, String status) {
        this.id = id;
        this.cliente = cliente;
        this.produtos = produtos;
        this.status = status;
    }
}
