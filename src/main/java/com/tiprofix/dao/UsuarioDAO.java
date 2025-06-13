package com.tiprofix.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.tiprofix.services.HashUtil;  
import com.tiprofix.models.Usuario;

public class UsuarioDAO extends DAO {

    public Usuario criarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, senha, cpf, cep, endereco, telefone, foto, foto_cpf, email, tipo) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
    
        try {
            conn = conectar();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Gera o hash da senha antes de salvar no banco
            String senhaHash = HashUtil.hashMD5(usuario.getSenha());  // Usando HashUtil para gerar o hash
            usuario.setSenha(senhaHash);  
    
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getSenha());
            pstmt.setString(3, usuario.getCpf());
            pstmt.setString(4, usuario.getCep());
            pstmt.setString(5, usuario.getEndereco());
            pstmt.setString(6, usuario.getTelefone());
            pstmt.setString(7, usuario.getFoto());
            pstmt.setString(8, usuario.getFoto_cpf());
            pstmt.setString(9, usuario.getEmail());
            pstmt.setString(10, usuario.getTipo());
    
            pstmt.executeUpdate();
            System.out.println("Usuario salvo com sucesso!");
    
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }
    
            return usuario;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuario: " + e.getMessage());
            throw new RuntimeException("Erro ao criar o usuario", e);
        } finally {
            fecharConexao(conn);
        }
    }
    

    public Usuario pegarUsuarioId(int id) {
        String sql = "SELECT id, nome, senha, cpf, cep, endereco, telefone, foto, foto_cpf, email, tipo FROM usuario WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Usuario usuario = null;

        try {
            conn = conectar();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("senha"),
                        rs.getString("cpf"),
                        rs.getString("cep"),
                        rs.getString("endereco"),
                        rs.getString("telefone"),
                        rs.getString("foto"),
                        rs.getString("foto_cpf"),
                        rs.getString("email"),
                        rs.getString("tipo"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuario: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar usuario", e);
        } finally {
            fecharConexao(conn);
        }

        return usuario;
    }

    public Usuario pegarUsuarioPorEmailESenha(String email, String senha) {
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuario WHERE email = ?")) {
    
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                // Recupera o hash da senha armazenado no banco
                String senhaHashArmazenada = rs.getString("senha");
    
                // Compara o hash da senha fornecida com o hash armazenado no banco
                if (HashUtil.verificarSenha(senha, senhaHashArmazenada)) {  // Verifica se a senha fornecida corresponde ao hash armazenado
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setCpf(rs.getString("cpf"));
                    usuario.setCep(rs.getString("cep"));
                    usuario.setEndereco(rs.getString("endereco"));
                    usuario.setTelefone(rs.getString("telefone"));
                    usuario.setFoto(rs.getString("foto"));
                    usuario.setFoto_cpf(rs.getString("foto_cpf"));
                    usuario.setTipo(rs.getString("tipo"));
                    return usuario;
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return null;
    }
    



    public List<Usuario> pegarTodosOsUsuarios() {
        String sql = "SELECT id, nome, senha, cpf, cep, endereco, telefone, foto, foto_cpf, email, tipo FROM usuario";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Usuario> usuarios = new ArrayList<>();

        try {
            conn = conectar();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("senha"),
                        rs.getString("cpf"),
                        rs.getString("cep"),
                        rs.getString("endereco"),
                        rs.getString("telefone"),
                        rs.getString("foto"),
                        rs.getString("foto_cpf"),
                        rs.getString("email"),
                        rs.getString("tipo"));

                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuario: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar usuario", e);
        } finally {
            fecharConexao(conn);
        }

        return usuarios;
    }

    public boolean atualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, senha = ?, cpf = ?, cep = ?, endereco = ?, telefone = ?, foto = ?, foto_cpf = ?, email = ?, tipo = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = conectar();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getSenha());
            pstmt.setString(3, usuario.getCpf());
            pstmt.setString(4, usuario.getCep());
            pstmt.setString(5, usuario.getEndereco());
            pstmt.setString(6, usuario.getTelefone());
            pstmt.setString(7, usuario.getFoto());
            pstmt.setString(8, usuario.getFoto_cpf());
            pstmt.setString(9, usuario.getEmail());
            pstmt.setString(10, usuario.getTipo());
            pstmt.setInt(11, usuario.getId());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas == 0) return false;

            System.out.println("Usuario atualizado com sucesso!");
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuario: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar o usuario", e);
        } finally {
            fecharConexao(conn);
        }
    }

    public boolean deletarUsuario(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        Boolean foiDeletado = false;

        try {
            conn = conectar();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            foiDeletado = pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error ao deletar usuario");
        } finally {
            fecharConexao(conn);
        }

        return foiDeletado;
    }
}
