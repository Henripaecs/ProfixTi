// Funções para consumir a API de pedidos do backend Spark

const API_PEDIDOS_URL = 'http://localhost:8081/pedido';

// Buscar todos os pedidos
function buscarPedidos() {
  return fetch(API_PEDIDOS_URL)
    .then(res => res.json());
}

// Criar um novo pedido
function criarPedido(pedido) {
  return fetch(API_PEDIDOS_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(pedido)
  })
  .then(res => res.json());
}

// Atualizar um pedido existente
function atualizarPedido(id, pedidoAtualizado) {
  return fetch(`${API_PEDIDOS_URL}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(pedidoAtualizado)
  })
  .then(res => res.json());
}

// Deletar um pedido
function deletarPedido(id) {
  return fetch(`${API_PEDIDOS_URL}/${id}`, {
    method: 'DELETE' })
    .then(res => res.status === 204);
}

// Função para buscar todos os pedidos do cliente logado
async function buscarPedidosCliente() {
    try {
        const response = await fetch('http://localhost:8081/pedido', {
            credentials: 'include'
        });
        
        if (!response.ok) {
            throw new Error('Erro ao buscar pedidos');
        }
        
        const pedidos = await response.json();
        return pedidos;
    } catch (error) {
        console.error('Erro ao buscar pedidos:', error);
        return [];
    }
}

// Função para criar o card de um pedido
function criarPedidoCard(pedido) {
    const statusClass = {
        'pendente': 'pendente',
        'aceito': 'aceito',
        'rejeitado': 'rejected',
        'concluido': 'completed'
    }[pedido.status?.toLowerCase()] || 'pendente';

    const statusText = {
        'pendente': 'Pendente',
        'aceito': 'Aceito',
        'rejeitado': 'Rejeitado',
        'concluido': 'Concluído'
    }[pedido.status?.toLowerCase()] || 'Pendente';

    // Adiciona botão de cancelar se pendente, no canto inferior direito
    const cancelarBtn = (pedido.status?.toLowerCase() === 'pendente') ?
        `<div style="position: absolute; right: 24px; bottom: 24px;">
            <button class="btn btn-outline-danger cancelar-pedido-btn" style="font-weight: 500; min-width: 160px; box-shadow: 0 2px 8px rgba(220,53,69,0.08);" data-id="${pedido.idPedido}">
                <i class="bi bi-x-circle"></i> Cancelar Pedido
            </button>
        </div>` : '';

    return `
        <div class="col-lg-8 mb-4">
            <div class="pedido-card shadow-lg" style="position: relative; min-height: 260px; border-radius: 18px; background: #fff; box-shadow: 0 4px 24px rgba(44,62,80,0.08); padding: 0; overflow: hidden; border: 1.5px solid #e3e6f0;">
                <div class="pedido-header d-flex justify-content-between align-items-center px-4 pt-4 pb-2" style="background: linear-gradient(90deg, #f8fafc 60%, #e3e6f0 100%); border-top-left-radius: 18px; border-top-right-radius: 18px;">
                    <h3 class="mb-0" style="font-size: 1.3rem; color: #5e2d53; font-weight: 700;">${pedido.habilidadeRequisitada || 'Categoria não informada'}</h3>
                    <span class="pedido-status ${statusClass}" style="font-size: 1rem; font-weight: 600; padding: 6px 18px; border-radius: 16px; letter-spacing: 0.5px; box-shadow: 0 1px 4px rgba(0,0,0,0.04);">${statusText}</span>
                </div>
                <div class="pedido-body d-flex flex-row gap-3 px-4 py-3" style="align-items: flex-start;">
                    ${pedido.imagem ? `<img src="${pedido.imagem}" alt="Imagem do serviço" style="width: 120px; height: 120px; object-fit: cover; border-radius: 12px; box-shadow: 0 2px 8px rgba(44,62,80,0.10); border: 1px solid #eee;">` : ''}
                    <div style="flex: 1; min-width: 0;">
                        <p style="margin-bottom: 6px;"><strong>Descrição:</strong> <span style="color: #444;">${pedido.descricao || ''}</span></p>
                        <p style="margin-bottom: 6px;"><strong>Endereço:</strong> <span style="color: #444;">${pedido.enderecoCliente || ''}</span></p>
                        <p style="margin-bottom: 6px;"><strong>Disponibilidade:</strong> <span style="color: #444;">${pedido.disponibilidade || ''}</span></p>
                        <p style="margin-bottom: 6px;"><strong>Telefone:</strong> <span style="color: #444;">${pedido.telefoneContato || ''}</span></p>
                        ${pedido.valor ? `<p style="margin-bottom: 6px;"><strong>Valor:</strong> <span style="color: #444;">R$ ${pedido.valor}</span></p>` : ''}
                    </div>
                </div>
                ${cancelarBtn}
            </div>
        </div>
    `;
}

// Função para carregar e exibir os pedidos
async function carregarPedidos() {
    const pedidoList = document.getElementById('pedido-list');
    const pedidoListEmpty = document.getElementById('pedido-list-empty');
    try {
        // Buscar usuário logado
        let user = null;
        try {
            let res = await fetch('http://localhost:8081/usuario-logado', { credentials: 'include' });
            if (res.ok) user = await res.json();
        } catch (e) {}
        const pedidos = await buscarPedidosCliente();
        // Filtrar pedidos do cliente logado
        const pedidosCliente = user ? pedidos.filter(p => p.nome === user.nome) : [];
        if (pedidosCliente.length === 0) {
            pedidoList.style.display = 'none';
            pedidoListEmpty.style.display = 'block';
            return;
        }
        pedidoList.style.display = 'flex';
        pedidoListEmpty.style.display = 'none';
        pedidoList.innerHTML = pedidosCliente.map(pedido => criarPedidoCard(pedido)).join('');
        // Adiciona evento aos botões de cancelar
        document.querySelectorAll('.cancelar-pedido-btn').forEach(btn => {
            btn.addEventListener('click', async function() {
                if (confirm('Tem certeza que deseja cancelar este pedido?')) {
                    const id = this.getAttribute('data-id');
                    const sucesso = await deletarPedido(id);
                    if (sucesso) {
                        alert('Pedido cancelado com sucesso!');
                        carregarPedidos();
                    } else {
                        alert('Erro ao cancelar pedido.');
                    }
                }
            });
        });
    } catch (error) {
        console.error('Erro ao carregar pedidos:', error);
        pedidoList.style.display = 'none';
        pedidoListEmpty.style.display = 'block';
    }
}

// Carregar pedidos quando a página for carregada
document.addEventListener('DOMContentLoaded', carregarPedidos);

// Exemplo de uso (remova ou adapte conforme necessário):
// buscarPedidos().then(console.log);
// criarPedido({ descricao: 'Novo pedido', ... }).then(console.log);
// atualizarPedido(1, { descricao: 'Atualizado', ... }).then(console.log);
// deletarPedido(1).then(console.log); 