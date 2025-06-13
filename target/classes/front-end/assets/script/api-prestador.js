// Funções para consumir a API de prestadores do backend Spark

const API_PRESTADOR_URL = 'http://localhost:8081/prestador';

// Buscar todos os prestadores
function buscarPrestadores() {
  return fetch(API_PRESTADOR_URL)
    .then(res => res.json());
}

// Criar um novo prestado
function criarPrestador(prestador) {
  return fetch(API_PRESTADOR_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(prestador)
  })
  .then(res => res.json());
}

// Atualizar um prestador existente
function atualizarPrestador(id, prestadorAtualizado) {
  return fetch(`${API_PRESTADOR_URL}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(prestadorAtualizado)
  })
  .then(res => res.json());
}

// Deletar um prestador
function deletarPrestador(id) {
  return fetch(`${API_PRESTADOR_URL}/${id}`, {
    method: 'DELETE'
  })
  .then(res => res.status === 204);
}

// Exemplo de uso (remova ou adapte conforme necessário):
// buscarPrestadores().then(console.log);
// criarPrestador({ nome: 'João', senha: '123', cpf: '123.456.789-00', cep: '12345-678', endereco: 'Rua X', telefone: '99999-9999', foto: 'img.png', tempoExperiencia: 5, habilidade: 'Eletricista' }).then(console.log);
// atualizarPrestador(1, { nome: 'Atualizado', habilidade: 'Pintor' }).then(console.log);
// deletarPrestador(1).then(console.log);

buscarPrestadores().then(console.log);
