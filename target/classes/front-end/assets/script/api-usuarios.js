// Funções para consumir a API de usuários do backend Spark

const API_URL = 'http://localhost:8081/usuarios';

// Buscar todos os usuários
function buscarUsuarios() {
  return fetch(API_URL)
    .then(res => res.json());
}

// Criar um novo usuário
function criarUsuario(usuario) {
  return fetch(API_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(usuario)
  })
  .then(res => res.json());
}

// Atualizar um usuário existente
function atualizarUsuario(id, usuarioAtualizado) {
  return fetch(`${API_URL}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(usuarioAtualizado)
  })
  .then(res => res.json());
}

// Deletar um usuário
function deletarUsuario(id) {
  return fetch(`${API_URL}/${id}`, {
    method: 'DELETE' })
    .then(res => res.status === 204);
}

// Exemplo de uso (remova ou adapte conforme necessário):
// buscarUsuarios().then(console.log);
// criarUsuario({ nome: 'Novo', email: 'novo@email.com' }).then(console.log);
// atualizarUsuario(1, { nome: 'Atualizado', email: 'atualizado@email.com' }).then(console.log);
// deletarUsuario(1).then(console.log);

buscarUsuarios().then(console.log);
criarPrestador({ nome: 'João', email: 'joao@email.com' }).then(console.log);
buscarPedidos().then(console.log); 