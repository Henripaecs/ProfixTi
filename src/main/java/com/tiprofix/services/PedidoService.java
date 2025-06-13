package com.tiprofix.services;

import java.util.List;

import com.tiprofix.dao.PedidoDAO;
import com.tiprofix.models.Pedido;

public class PedidoService {
    private PedidoDAO dao;

    public PedidoService() {
        dao = new PedidoDAO();
    }

    public Pedido criaPedido(Pedido pedido) {
        return dao.criarPedido(pedido);
    }

    public List<Pedido> pegarTodosOsPedidos() {
        return dao.pegarTodosOsPedidos();
    }

    public Pedido pegarPedidoId(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("ID inválido");
        }

        var pedido = dao.pegarPedidoId(id);
        return pedido;
    }

    public boolean atualizarPedido(Pedido pedido, int id) {
        if (id < 1) {
            throw new IllegalArgumentException("ID inválido");
        }

        var existente = pegarPedidoId(id);
        if (existente == null)
            return false;

        pedido.setIdPedido(existente.getIdPedido());
        return dao.atualizarPedido(pedido);
    }

    public boolean deletarPedido(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("ID inválido");
        }

        return dao.deletarPedido(id);
    }

    public boolean atualizarStatusPedido(int id, String status) {
        if (id < 1) {
            throw new IllegalArgumentException("ID inválido");
        }
        return dao.atualizarStatusPedido(id, status);
    }
}
