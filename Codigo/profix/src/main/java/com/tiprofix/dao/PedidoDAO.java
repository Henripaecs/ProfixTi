package com.tiprofix.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.util.ArrayList;
import java.util.List;

import com.tiprofix.models.Pedido;

public class PedidoDAO extends DAO {

    // Insere um novo pedido no banco
    public Pedido criarPedido(Pedido pedido) {
        String sql = "INSERT INTO pedidos (nome, habilidade_requisitada, descricao, endereco_cliente, telefone_contato, valor, status, disponibilidade, imagem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, pedido.getNome());
            pstmt.setString(2, pedido.getHabilidadeRequisitada());
            pstmt.setString(3, pedido.getDescricao());
            pstmt.setString(4, pedido.getEnderecoCliente());
            pstmt.setString(5, pedido.getTelefoneContato());
            pstmt.setBigDecimal(6, pedido.getValor());
            pstmt.setString(7, pedido.getStatus() != null ? pedido.getStatus() : "pendente");
            pstmt.setString(8, pedido.getDisponibilidade());
            pstmt.setString(9, pedido.getImagem());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setIdPedido(rs.getInt(1));
                }
            }

            System.out.println("Pedido salvo com sucesso!");
            return pedido;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir pedido: " + e.getMessage());
            throw new RuntimeException("Erro ao criar o pedido", e);
        }
    }

    // Busca pedido pelo ID
    public Pedido pegarPedidoId(int id) {
        String sql = "SELECT id_pedido, nome, habilidade_requisitada, descricao, endereco_cliente, telefone_contato, valor, status, disponibilidade, imagem FROM pedidos WHERE id_pedido = ?";
        Pedido pedido = null;

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    pedido = new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getString("nome"),
                        rs.getString("habilidade_requisitada"),
                        rs.getString("descricao"),
                        rs.getString("endereco_cliente"),
                        rs.getString("telefone_contato"),
                        rs.getString("disponibilidade"),
                        rs.getString("imagem"),
                        rs.getBigDecimal("valor"),
                        rs.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar pedido: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar pedido", e);
        }

        return pedido;
    }

    // Lista todos os pedidos
    public List<Pedido> pegarTodosOsPedidos() {
        String sql = "SELECT id_pedido, nome, habilidade_requisitada, descricao, endereco_cliente, telefone_contato, valor, status, disponibilidade, imagem FROM pedidos";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Pedido pedido = new Pedido(
                    rs.getInt("id_pedido"),
                    rs.getString("nome"),
                    rs.getString("habilidade_requisitada"),
                    rs.getString("descricao"),
                    rs.getString("endereco_cliente"),
                    rs.getString("telefone_contato"),
                    rs.getString("disponibilidade"),
                    rs.getString("imagem"),
                    rs.getBigDecimal("valor"),
                    rs.getString("status")
                );
                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar pedidos: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar pedidos", e);
        }

        return pedidos;
    }

    // Atualiza pedido
    public boolean atualizarPedido(Pedido pedido) {
        String sql = "UPDATE pedidos SET nome = ?, habilidade_requisitada = ?, descricao = ?, endereco_cliente = ?, telefone_contato = ?, valor = ?, disponibilidade = ?, imagem = ? WHERE id_pedido = ?";

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, pedido.getNome());
            pstmt.setString(2, pedido.getHabilidadeRequisitada());
            pstmt.setString(3, pedido.getDescricao());
            pstmt.setString(4, pedido.getEnderecoCliente());
            pstmt.setString(5, pedido.getTelefoneContato());
            pstmt.setBigDecimal(6, pedido.getValor());
            pstmt.setString(7, pedido.getDisponibilidade());
            pstmt.setString(8, pedido.getImagem());
            pstmt.setInt(9, pedido.getIdPedido());

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas == 0) {
                return false;
            }

            System.out.println("Pedido atualizado com sucesso!");
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar pedido: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar o pedido", e);
        }
    }

    // Atualiza status do pedido
    public boolean atualizarStatusPedido(int id, String status) {
        // Troca status aceito/pendente para português
        if ("accepted".equalsIgnoreCase(status)) status = "aceito";
        if ("pending".equalsIgnoreCase(status)) status = "pendente";
        String sql = "UPDATE pedidos SET status = ? WHERE id_pedido = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            int linhasAfetadas = pstmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status do pedido: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar status do pedido", e);
        }
    }

    // Deleta pedido
    public boolean deletarPedido(int id) {
        String sql = "DELETE FROM pedidos WHERE id_pedido = ?";
        boolean foiDeletado = false;

        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            foiDeletado = pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar pedido: " + e.getMessage());
            throw new RuntimeException("Erro ao deletar pedido", e);
        }

        return foiDeletado;
    }
}
