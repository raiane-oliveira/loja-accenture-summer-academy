package com.academia.loja_accenture.modulos.usuario.domain;

import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import com.academia.loja_accenture.modulos.pedido.domain.Produto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adiciona classe Vendedor para mapeamento no banco de dados MySQL.
 *
 * @author Bruna Neves
 */
@Entity
@Table(name = "vendedor")
@Data
public class Vendedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String setor;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String senha;
    
    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL)
    private List<Produto> produtos = new ArrayList<>();
    
    @OneToMany(mappedBy = "vendedor")
    private Set<Pedido> pedidos = new HashSet<>();
}
