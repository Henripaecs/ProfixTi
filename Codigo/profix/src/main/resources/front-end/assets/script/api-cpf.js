<script>
document.getElementById("registro-form").addEventListener("submit", function(event) {
  event.preventDefault(); // Evita envio automático

  const cpf = document.getElementById("cpf").value.replace(/\D/g, ""); // remove pontos e traço
  const nome = document.getElementById("nome").value;
  const mensagem = document.getElementById("mensagem");

  // Validação simples de CPF
  if (cpf.length !== 11) {
    mensagem.textContent = "CPF inválido (formato).";
    return;
  }

  
  const url = `https://api.receitaws.com.br/v1/cpf/${cpf}`;

  fetch(url, {
    method: "GET",
    headers: {
      "Accept": "application/json"
    }
  })
  .then(response => {
    if (!response.ok) {
      throw new Error("Erro ao consultar o CPF");
    }
    return response.json();
  })
  .then(data => {
    if (data.status === "OK" && data.nome.toLowerCase().includes(nome.toLowerCase().split(" ")[0])) {
      mensagem.textContent = "CPF válido. Registro pode continuar.";
      
      // Aqui você poderia fazer o envio para seu backend, por exemplo:
      // fetch('/api/registrar', { method: "POST", body: JSON.stringify({ nome, cpf }) });

    } else {
      mensagem.textContent = "CPF inválido ou não corresponde ao nome informado.";
    }
  })
  .catch(error => {
    mensagem.textContent = "Erro na validação do CPF.";
    console.error(error);
  });
});
</script>
