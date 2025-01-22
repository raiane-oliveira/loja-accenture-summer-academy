package com.academia.loja_accenture.modulos.pagamento.domain;

import java.io.Serializable;
import java.util.Date;
/**
 * Classe Pagamento representa um pagamento realizado para um pedido no sistema.
 * Mapeia as informações de um pagamento, como o valor, o método de pagamento,
 * a data de pagamento e a confirmação do mesmo.
 *
 * @author Bruna Neves
 */
public class Pagamento implements Serializable {
    private int id;
    private int pedidoId;
    private double valor;
    private String metodo;
    private Date dataPagamento;
    private boolean confirmado;
}
