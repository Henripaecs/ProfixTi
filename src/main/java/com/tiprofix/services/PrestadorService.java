package com.tiprofix.services;

import java.util.List;

import com.tiprofix.dao.PrestadorDAO;
import com.tiprofix.models.Prestador;

public class PrestadorService {
    private PrestadorDAO dao;

    public PrestadorService() {
        dao = new PrestadorDAO();
    }

    public Prestador criaPrestador(Prestador pt) {
        return dao.criarPrestador(pt);
    }

    public List<Prestador> pegarTodosOsPrestadores() {
        return dao.pegarTodosOsPrestadores();
    }

    public Prestador pegarPrestadorId(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("ID inválido");
        }

        var prestador = dao.pegarPrestadorPorId(id);
        return prestador;
    }

    public boolean atualizarPrestador(Prestador pt, int id) {
        if (id < 1) {
            throw new IllegalArgumentException("ID inválido");
        }

        var existente = pegarPrestadorId(id);
        if (existente == null)
            return false;

        pt.setId(existente.getId());
        return dao.atualizarPrestador(pt);
    }

    public boolean deletarPrestador(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("ID inválido");
        }

        return dao.deletarPrestador(id);
    }

    public Prestador autenticar(String email, String senha) {
        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Email e senha são obrigatórios");
        }
        return dao.pegarPrestadorPorEmailESenha(email, senha);
    }

}
