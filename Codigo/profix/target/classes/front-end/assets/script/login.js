document.getElementById('loginForm').addEventListener('submit', function (e) {
  e.preventDefault();

  const email = document.getElementById('email').value;
  const senha = document.getElementById('password').value; // corrigido
  const profile = document.getElementById('profile').value;

  if (!email || !senha) {
    alert("Preencha todos os campos!");
    return;
  }

  login(email, senha, profile)
    .then(usuario => {
      if (!usuario || usuario.erro) {
        alert("Falha no login: " + (usuario?.erro || 'Credenciais inválidas.'));
        return;
      }
      if (usuario.tipo === "Cliente") {
        window.location.href = "client-profile.html";
      } else if (usuario.tipo === "Prestador") {
        window.location.href = "provider-dashboard.html";
      } else {
        // Tipo de usuário desconhecido, apenas loga no console
        console.warn("Tipo de usuário desconhecido.", usuario);
      }
    })
    .catch(err => {
      if (err && err.message && err.message.includes('Failed to fetch')) {
        // Não exibe alerta se foi redirecionado corretamente
        return;
      }
      alert("Falha no login: " + (err?.message || 'Erro desconhecido.'));
    });
});

function login(email, senha, profile) {
  const endpoint = profile === 'Prestador' ? '/prestador/login/prestador' : '/login';

  return fetch(`http://localhost:8081${endpoint}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({ email, senha })
  })
    .then(response => {
      if (!response.ok) {
        return response.json().then(err => {
          throw new Error(err.erro || 'Erro ao fazer login');
        });
      }
      return response.json();
    });
}
