package com.tiprofix.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.tiprofix.models.Prestador;

public class PrestadorDAO extends DAO {

    public Prestador criarPrestador(Prestador prestador) {
        String sql = "INSERT INTO prestador_de_servico (nome, senha, cpf, cep, endereco, telefone, foto, foto_cpf, tempo_experiencia, habilidade, tipo, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = conectar();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, prestador.getNome());
            pstmt.setString(2, prestador.getSenha());
            pstmt.setString(3, prestador.getCpf());
            pstmt.setString(4, prestador.getCep());
            pstmt.setString(5, prestador.getEndereco());
            pstmt.setString(6, prestador.getTelefone());
            pstmt.setString(7, prestador.getFoto());
            pstmt.setString(8, prestador.getFoto_cpf());
            pstmt.setInt(9, prestador.getTempoExperiencia());
            pstmt.setString(10, prestador.getHabilidade());
            pstmt.setString(11, prestador.getTipo());
            pstmt.setString(12, prestador.getEmail());

            pstmt.executeUpdate();
            System.out.println("Prestador salvo com sucesso!");

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                prestador.setId(rs.getInt(1));
            }

            return prestador;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir prestador: " + e.getMessage());
            throw new RuntimeException("Erro ao criar o prestador", e);
        } finally {
            fecharConexao(conn);
        }
    }

    public Prestador pegarPrestadorPorId(int id) {
        String sql = "SELECT * FROM prestador_de_servico WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Prestador prestador = null;

        try {
            conn = conectar();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                prestador = new Prestador(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("senha"),
                        rs.getString("cpf"),
                        rs.getString("cep"),
                        rs.getString("endereco"),
                        rs.getString("telefone"),
                        rs.getString("foto"),
                        rs.getString("foto_cpf"),
                        rs.getInt("tempo_experiencia"),
                        rs.getString("habilidade"),
                        rs.getString("tipo"),
                        rs.getString("email")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar prestador: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar prestador", e);
        } finally {
            fecharConexao(conn);
        }

        return prestador;
    }

    public List<Prestador> pegarTodosOsPrestadores() {
        String sql = "SELECT * FROM prestador_de_servico";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Prestador> prestadores = new ArrayList<>();

        try {
            conn = conectar();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Prestador prestador = new Prestador(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("senha"),
                        rs.getString("cpf"),
                        rs.getString("cep"),
                        rs.getString("endereco"),
                        rs.getString("telefone"),
                        rs.getString("foto"),
                        rs.getString("foto_cpf"),
                        rs.getInt("tempo_experiencia"),
                        rs.getString("habilidade"),
                        rs.getString("tipo"),
                        rs.getString("email")
                );
                prestadores.add(prestador);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar prestadores: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar prestadores", e);
        } finally {
            fecharConexao(conn);
        }

        return prestadores;
    }

    public boolean atualizarPrestador(Prestador prestador) {
        String sql = "UPDATE prestador_de_servico SET nome = ?, senha = ?, cpf = ?, cep = ?, endereco = ?, telefone = ?, foto = ?, foto_cpf = ?, tempo_experiencia = ?, habilidade = ?, tipo = ?, email = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = conectar();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, prestador.getNome());
            pstmt.setString(2, prestador.getSenha());
            pstmt.setString(3, prestador.getCpf());
            pstmt.setString(4, prestador.getCep());
            pstmt.setString(5, prestador.getEndereco());
            pstmt.setString(6, prestador.getTelefone());
            pstmt.setString(7, prestador.getFoto());
            pstmt.setString(8, prestador.getFoto_cpf());
            pstmt.setInt(9, prestador.getTempoExperiencia());
            pstmt.setString(10, prestador.getHabilidade());
            pstmt.setString(11, prestador.getTipo());
            pstmt.setString(12, prestador.getEmail());
            pstmt.setInt(13, prestador.getId());

            int linhasAfetadas = pstmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar prestador: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar prestador", e);
        } finally {
            fecharConexao(conn);
        }
    }

    public Prestador pegarPrestadorPorEmailESenha(String email, String senha) {
        try (Connection conn = conectar();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM prestador_de_servico WHERE email = ? AND senha = ?")) {

            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Prestador(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("senha"),
                    rs.getString("cpf"),
                    rs.getString("cep"),
                    rs.getString("endereco"),
                    rs.getString("telefone"),
                    rs.getString("foto"),
                    rs.getString("foto_cpf"),
                    rs.getInt("tempo_experiencia"),
                    rs.getString("habilidade"),
                    rs.getString("tipo"),
                    rs.getString("email")
                );
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



    public boolean deletarPrestador(int id) {
        String sql = "DELETE FROM prestador_de_servico WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = conectar();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int linhasAfetadas = pstmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar prestador: " + e.getMessage());
            throw new RuntimeException("Erro ao deletar prestador", e);
        } finally {
            fecharConexao(conn);
        }
    }
}
