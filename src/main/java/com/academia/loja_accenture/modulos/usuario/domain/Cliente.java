package com.academia.loja_accenture.modulos.usuario.domain;

import com.academia.loja_accenture.modulos.pedido.domain.Pedido;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Adiciona classe Cliente para mapeamento no banco de dados MySQL.
 *
 * @author Bruna Neves
 */
@Entity
@Table(name = "cliente")
@Data
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String senha;
    
    @OneToMany(mappedBy = "cliente")
    private Set<Pedido> pedidos = new HashSet<>();
}
