package com.tiprofix.models;

public class Usuario {
    private int id;
    private String nome;
    private String senha;
    private String cpf;
    private String cep;
    private String endereco;
    private String telefone;
    private String foto;
    private String foto_cpf;
    private String email;
    private String tipo;

    public Usuario() {
    }

    public Usuario(int id, String nome, String senha, String cpf, String cep, String endereco, String telefone, String foto, String foto_cpf, String email, String tipo) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.cpf = cpf;
        this.cep = cep;
        this.endereco = endereco;
        this.telefone = telefone;
        this.foto = foto;
        this.foto_cpf = foto_cpf;
        this.email = email;
        this.tipo = tipo;
    }


    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nome=" + nome + ", senha=" + senha + ", cpf=" + cpf + ", cep=" + cep
                + ", endereco=" + endereco + ", telefone=" + telefone + ", foto=" + foto + ", foto_cpf=" + foto_cpf + ", email=" + email
                + ", tipo=" + tipo + "]";
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFoto_cpf() {
        return foto_cpf;
    }

    public void setFoto_cpf(String foto_cpf) {
        this.foto_cpf = foto_cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
    