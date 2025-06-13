function processImage() {
    console.log("Analisando a imagem...");

    // Verifique se você tem a chave da API e o URL corretos
    var subscriptionKey = "8OwrK6rtVWCyswJV3LZwcy1TIhtnxwVOALvQJjCLsebU7akPfC8hJQQJ99BEACYeBjFXJ3w3AAAFACOGQbKi";  // Substitua com a chave de sua API
    var aplicationUrl = "https://eastus.api.cognitive.microsoft.com/vision/v3.0/ocr"; // Endpoint para o OCR da Azure

    var fileInput = document.getElementById("cpfPhoto");
    var file = fileInput.files[0];  // Pega o arquivo carregado
    if (!file) {
        alert("Por favor, selecione uma imagem.");
        return;
    }

    // Cria um objeto FormData para enviar o arquivo como multipart/form-data
    var formData = new FormData();
    formData.append("file", file);

    // Faz a requisição para a API de OCR usando FormData (multipart/form-data)
    $.ajax({
        url: aplicationUrl,
        beforeSend: function(xhrObj) {
            xhrObj.setRequestHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
        },
        type: "POST",
        data: formData,
        processData: false,  // Não processar os dados do FormData
        contentType: false,  // Não definir o tipo de conteúdo, pois é automaticamente tratado pelo FormData
    })
    .done(function(data) {
        var extractedText = "";
        if (data.regions) {
            data.regions.forEach(function(region) {
                region.lines.forEach(function(line) {
                    line.words.forEach(function(word) {
                        extractedText += word.text + " ";  
                    });
                    extractedText += "\n";  
                });
            });
            processExtractedText(extractedText);  // Chama a função para processar o texto extraído
        } else {
            alert("Nenhum texto extraído.");
        }
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
        var errorString = (errorThrown === "") ? "Error." : errorThrown + " (" + jqXHR.status + "): ";
        errorString += (jqXHR.responseText === "") ? "" : (jQuery.parseJSON(jqXHR.responseText).message) ? jQuery.parseJSON(jqXHR.responseText).message : jQuery.parseJSON(jqXHR.responseText).error.message;
        alert(errorString);  
    });
}

function processExtractedText(text) {
    text = text.replace(/\s+/g, ' ').trim();  // Remove espaços extras e ajusta o texto

    var cpfPattern = /(\d{3})[.\s-]?(\d{3})[.\s-]?(\d{3})[.\s-]?(\d{2})/;
    var cpfMatch = text.match(cpfPattern);  // Procura o CPF no texto extraído

    if (cpfMatch) {
        var cpf = cpfMatch[0].replace(/\s+/g, '');  // Remove os espaços extras do CPF
        document.getElementById("cpfText").textContent = cpf;  // Exibe o CPF extraído
        document.getElementById("cpfButtons").style.display = 'block';  // Exibe os botões para verificação
        window.foundCpf = cpf;  // Armazena o CPF extraído globalmente
    } else {
        alert("CPF não encontrado.");
        document.getElementById("cpfButtons").style.display = 'none';  // Esconde os botões caso o CPF não seja encontrado
    }
}

// Função para comparar o CPF digitado com o CPF extraído da imagem
function checkCpf(correct) {
    var cpfDigitado = document.getElementById("cpf").value.replace(/\D/g, '');  // Remove qualquer caractere não numérico do CPF digitado
    var cpfExtraido = window.foundCpf.replace(/\D/g, '');  // Remove qualquer caractere não numérico do CPF extraído

    console.log("CPF Digitado:", cpfDigitado); // Para depuração
    console.log("CPF Extraído:", cpfExtraido); // Para depuração

    var correctMessage = correct ? "CPF Correto!" : "CPF Errado!";
    alert(correctMessage);  // Exibe uma mensagem de confirmação

    // Comparando os dois CPFs após remover formatação e espaços
    if (cpfDigitado === cpfExtraido) {
        alert("Os CPFs coincidem!");
        // Aqui você pode continuar o fluxo do cadastro, por exemplo, habilitar o botão de "Cadastrar"
    } else {
        alert("Os CPFs não coincidem! Tente novamente.");
        document.getElementById("cpfText").textContent = "CPF Errado! Tente novamente.";  // Alerta visual
    }
}
