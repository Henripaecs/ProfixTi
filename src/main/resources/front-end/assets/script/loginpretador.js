fetch("http://localhost:8081/login/prestador", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  credentials: "include",
  body: JSON.stringify({
    email: "prestador@email.com",
    senha: "senha123"
  })
})
.then(res => res.json())
.then(data => {
  if (data.erro) {
    alert(data.erro);
  } else {
    console.log("Login do prestador:", data);
    localStorage.setItem("userType", "provider");
    localStorage.setItem("userId", data.id);
    localStorage.setItem("userName", data.nome);
    window.location.href = "prestador.html";
  }
});
