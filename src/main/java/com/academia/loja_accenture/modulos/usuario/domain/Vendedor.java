package com.academia.loja_accenture.modulos.usuario.domain;

/**
 * Adiciona classe Vendedor para mapeamento no banco de dados MySQL.
 *
 * @author Bruna Neves
 */

public class Vendedor {
    private int id;
    private String nome;
    private String setor;

    public Vendedor() {}

    public Vendedor(String nome, String setor) {
        this.nome = nome;
        this.setor = setor;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSetor() {
        return setor;
    }
    public void setSetor(String setor) {
        this.setor = setor;
    }
}
