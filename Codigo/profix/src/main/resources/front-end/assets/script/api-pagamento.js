const express = require('express');
const path = require('path');
const mercadopago = require('mercadopago');

const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(express.static(path.join(__dirname, 'public')));

// Configure sua Access Token do Mercado Pago
mercadopago.configure({
  access_token: 'SEU_ACCESS_TOKEN_AQUI'
});

// Rota para criar o pagamento
app.post('/create-payment', async (req, res) => {
  const { nome, email } = req.body;

  try {
    const preference = await mercadopago.preferences.create({
      items: [{
        title: "Taxa de Registro - Profix",
        quantity: 1,
        unit_price: 10.00
      }],
      payer: { email },
      back_urls: {
        success: `http://localhost:3000/sucesso?nome=${encodeURIComponent(nome)}&email=${encodeURIComponent(email)}`,
        failure: "http://localhost:3000/erro"
      },
      auto_return: "approved"
    });

    res.json({ init_point: preference.body.init_point });
  } catch (error) {
    console.error(error);
    res.status(500).send("Erro ao criar pagamento");
  }
});

// Rota de sucesso após pagamento
app.get('/sucesso', (req, res) => {
  const { nome, email } = req.query;

  // Aqui você pode salvar o usuário no banco de dados!
  console.log(`Usuário ${nome} (${email}) foi cadastrado com sucesso!`);

  res.send(`<h2>Cadastro e pagamento concluídos com sucesso!</h2>`);
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Servidor rodando em http://localhost:${PORT}`);
});
