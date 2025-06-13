package com.tiprofix.models;

public class Prestador {
    private int id;
    private String nome;
    private String senha;
    private String cpf;
    private String cep;
    private String endereco;
    private String telefone;
    private String foto;
    private String foto_cpf;
    private int tempoExperiencia;
    private String habilidade;
    private String tipo;
    private String email; // novo campo

    // Construtor completo
    public Prestador(int id, String nome, String senha, String cpf, String cep, String endereco,
                     String telefone, String foto, String foto_cpf, int tempoExperiencia,
                     String habilidade, String tipo, String email) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.cpf = cpf;
        this.cep = cep;
        this.endereco = endereco;
        this.telefone = telefone;
        this.foto = foto;
        this.foto_cpf = foto_cpf;
        this.tempoExperiencia = tempoExperiencia;
        this.habilidade = habilidade;
        this.tipo = tipo;
        this.email = email;
    }

    // Construtor sem ID (útil para criação)
    public Prestador(String nome, String senha, String cpf, String cep, String endereco,
                     String telefone, String foto, String foto_cpf, int tempoExperiencia,
                     String habilidade, String tipo, String email) {
        this.nome = nome;
        this.senha = senha;
        this.cpf = cpf;
        this.cep = cep;
        this.endereco = endereco;
        this.telefone = telefone;
        this.foto = foto;
        this.foto_cpf = foto_cpf;
        this.tempoExperiencia = tempoExperiencia;
        this.habilidade = habilidade;
        this.tipo = tipo;
        this.email = email;
    }

    // Getters e Setters
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

    public int getTempoExperiencia() {
        return tempoExperiencia;
    }
    public void setTempoExperiencia(int tempoExperiencia) {
        this.tempoExperiencia = tempoExperiencia;
    }

    public String getHabilidade() {
        return habilidade;
    }
    public void setHabilidade(String habilidade) {
        this.habilidade = habilidade;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
