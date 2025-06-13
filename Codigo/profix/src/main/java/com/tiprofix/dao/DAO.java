package com.tiprofix.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe base de acesso a dados (DAO) responsável por gerenciar a conexão com o banco de dados PostgreSQL.
 * Pode ser estendida por outras classes DAO específicas para reutilizar os métodos de conexão e encerramento.
 */
public class DAO {

    protected Connection conexao;

    /**
     * Estabelece uma conexão com o banco de dados PostgreSQL hospedado na Azure.
     *
     * @return a conexão estabelecida com o banco de dados
     */
    protected Connection conectar() {
        String driverName = "org.postgresql.Driver";
        String serverName = "profix.postgres.database.azure.com";
        int porta = 5432;
        String mydatabase = "postgres"; 
        String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" + mydatabase;
        String username = "profix";
        String password = "Ti22025*!"; 

        try {
            // Carrega o driver PostgreSQL
            Class.forName(driverName);

            // Estabelece a conexão com o banco
            conexao = DriverManager.getConnection(url, username, password);
            System.out.println("Conexão efetuada com o PostgreSQL na Azure!");
            return conexao;

        } catch (ClassNotFoundException e) { 
            System.err.println("Driver do PostgreSQL não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao PostgreSQL: " + e.getMessage());
        }

        return null;
    }

    public DAO() {
        // Construtor padrão
    }

    /**
     * Fecha a conexão com o banco de dados, caso ela não seja nula.
     *
     * @param conn a conexão a ser fechada.
     */
    protected void fecharConexao(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexão fechada com sucesso!");
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
                throw new RuntimeException("Erro ao fechar a conexão com o banco de dados");
            }
        }
    }
}