function processImage() {
    var subscriptionKey = "8OwrK6rtVWCyswJV3LZwcy1TIhtnxwVOALvQJjCLsebU7akPfC8hJQQJ99BEACYeBjFXJ3w3AAAFACOGQbKi";  // Substitua com sua chave
    var aplicationUrl = "https://eastus.api.cognitive.microsoft.com/vision/v3.0/ocr"; // Endpoint para o OCR da Azure

    var fileInput = document.getElementById("foto_cpf");
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
            processExtractedText(extractedText);  
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
    text = text.replace(/\s+/g, ' ').trim();  

    var cpfPattern = /(\d{3})[.\s-]?(\d{3})[.\s-]?(\d{3})[.\s-]?(\d{2})/;
    var cpfMatch = text.match(cpfPattern);

    if (cpfMatch) {
        var cpf = cpfMatch[0].replace(/\s+/g, '');  // Remove os espaços extras do CPF
        document.getElementById("cpfText").textContent = cpf;
        document.getElementById("cpfButtons").style.display = 'block'; 
        window.foundCpf = cpf;
    } else {
        alert("CPF não encontrado.");
        document.getElementById("cpfButtons").style.display = 'none'; 
    }
}

// Função para comparar o CPF digitado com o CPF extraído da imagem
function checkCpf(correct) {
    // Remove qualquer caractere não numérico do CPF digitado (também do CPF extraído)
    var cpfDigitado = document.getElementById("cpf").value.replace(/\D/g, ''); // Remove todos os não-números do CPF digitado
    var cpfExtraido = window.foundCpf.replace(/\D/g, ''); // Remove todos os não-números do CPF extraído

    console.log("CPF Digitado:", cpfDigitado); // Para depuração
    console.log("CPF Extraído:", cpfExtraido); // Para depuração

    var correctMessage = correct ? "CPF Correto!" : "CPF Errado!";
    alert(correctMessage);

    // Comparando os dois CPFs após remover formatação e espaços
    if (cpfDigitado === cpfExtraido) {
        alert("Os CPFs coincidem!");
        // Aqui você pode continuar o fluxo do cadastro, por exemplo, habilitar o botão de "Cadastrar"
    } else {
        alert("Os CPFs não coincidem! Tente novamente.");
        document.getElementById("cpfText").textContent = "CPF Errado! Tente novamente.";  // Alerta visual
    }
}

