package com.tiprofix.services;

import java.util.List;

import com.tiprofix.dao.UsuarioDAO;
import com.tiprofix.models.Usuario;

public class UsuarioService {
    private UsuarioDAO dao;

    public UsuarioService() {
        dao = new UsuarioDAO();
    }

    public Usuario criaUsuario(Usuario usuario) {
        // Gerar o hash MD5 da senha (ou usar bcrypt aqui)
        String senhaHash = HashUtil.hashMD5(usuario.getSenha());
        usuario.setSenha(senhaHash);  // Atualiza a senha com o hash

        return dao.criarUsuario(usuario);
    }

    public List<Usuario> pegarTodosOsUsuarios() {
        return dao.pegarTodosOsUsuarios();
    }

    public Usuario pegarUsuarioId(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("ID inválido");
        }
        return dao.pegarUsuarioId(id);
    }

    public Usuario autenticar(String email, String senha) {
        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Email e senha são obrigatórios");
        }

        // Gerar o hash MD5 da senha fornecida (ou usar bcrypt aqui)
        String senhaHash = HashUtil.hashMD5(senha);

        return dao.pegarUsuarioPorEmailESenha(email, senhaHash);  // Passa o hash da senha
    }

    public boolean atualizarUsuario(Usuario usuario, int id) {
        if (id < 1) {
            throw new IllegalArgumentException("ID inválido");
        }

        Usuario existente = dao.pegarUsuarioId(id);
        if (existente == null) return false;

        usuario.setId(id);
        return dao.atualizarUsuario(usuario);
    }

    public boolean deletarUsuario(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("ID inválido");
        }
        return dao.deletarUsuario(id);
    }
}
