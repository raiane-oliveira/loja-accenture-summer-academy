package com.academia.loja_accenture.modulos.usuario.domain;

import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vendedor")
@Data
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, length = 50)
    private String setor;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String senha;

    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Produto> produtos = new ArrayList<>();

    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Pedido> pedidos = new HashSet<>();

    // Construtor padrão (necessário para o Jackson)
    public Vendedor() {}

    // Construtor completo (opcional, mas útil para testes ou inicialização)
    public Vendedor(Long id, String nome, String setor, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.setor = setor;
        this.email = email;
        this.senha = senha;
    }

    // Construtor adicional (sem ID, para criação de novos objetos)
    public Vendedor(String nome, String setor, String email, String senha) {
        this.nome = nome;
        this.setor = setor;
        this.email = email;
        this.senha = senha;
    }
}
