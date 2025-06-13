package com.tiprofix.models;

import java.math.BigDecimal;

public class Pedido {
    private int idPedido;
    private String nome;
    private String habilidadeRequisitada;
    private String descricao;
    private String enderecoCliente;
    private String telefoneContato;
    private BigDecimal valor;
    private String status;
    private String disponibilidade;
    private String imagem;

    public Pedido() {
        // Construtor vazio
    }

    public Pedido(int idPedido, String nome, String habilidadeRequisitada, String descricao,
                  String enderecoCliente, String telefoneContato, String disponibilidade, String imagem, java.math.BigDecimal valor, String status) {
        this.idPedido = idPedido;
        this.nome = nome;
        this.habilidadeRequisitada = habilidadeRequisitada;
        this.descricao = descricao;
        this.enderecoCliente = enderecoCliente;
        this.telefoneContato = telefoneContato;
        this.disponibilidade = disponibilidade;
        this.imagem = imagem;
        this.valor = valor;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Pedido [idPedido=" + idPedido + ", nome=" + nome + ", habilidadeRequisitada=" + habilidadeRequisitada +
               ", descricao=" + descricao + ", enderecoCliente=" + enderecoCliente + ", telefoneContato=" + telefoneContato +
               ", disponibilidade=" + disponibilidade + ", valor=" + valor + "]";
    }

    // Getters e Setters

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHabilidadeRequisitada() {
        return habilidadeRequisitada;
    }

    public void setHabilidadeRequisitada(String habilidadeRequisitada) {
        this.habilidadeRequisitada = habilidadeRequisitada;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEnderecoCliente() {
        return enderecoCliente;
    }

    public void setEnderecoCliente(String enderecoCliente) {
        this.enderecoCliente = enderecoCliente;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
