// Funções para máscaras de input
function maskCPF(cpf) {
    // Remove caracteres não numéricos
    cpf = cpf.replace(/\D/g, '');
    
    // Aplica a máscara
    if (cpf.length <= 3) {
        return cpf;
    } else if (cpf.length <= 6) {
        return cpf.replace(/(\d{3})(\d{0,3})/, "$1.$2");
    } else if (cpf.length <= 9) {
        return cpf.replace(/(\d{3})(\d{3})(\d{0,3})/, "$1.$2.$3");
    } else {
        return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{0,2})/, "$1.$2.$3-$4");
    }
}

function maskCEP(cep) {
    return cep.replace(/(\d{5})(\d{3})/, "$1-$2");
}

function maskPhone(phone) {
    return phone.replace(/(\d{2})(\d{5})(\d{4})/, "($1) $2-$3");
}

// Função para converter imagem em base64
function getBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    });
}

// Aplicar máscaras nos inputs
document.addEventListener('DOMContentLoaded', function() {
    const cpfInput = document.getElementById('cpf');
    const cepInput = document.getElementById('cep');
    const phoneInput = document.getElementById('phone');

    cpfInput.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length <= 11) {
            e.target.value = maskCPF(value);
        }
    });

    cepInput.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length <= 8) {
            e.target.value = maskCEP(value);
        }
    });

    phoneInput.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length <= 11) {
            e.target.value = maskPhone(value);
        }
    });
});

// Função para validar CPF
function validarCPF(cpf) {
    cpf = cpf.replace(/[^\d]/g, '');
    
    if (cpf.length !== 11) return false;
    
    // Verifica se todos os dígitos são iguais
    if (/^(\d)\1+$/.test(cpf)) return false;
    
    // Validação do primeiro dígito verificador
    let soma = 0;
    for (let i = 0; i < 9; i++) {
        soma += parseInt(cpf.charAt(i)) * (10 - i);
    }
    let resto = 11 - (soma % 11);
    let digitoVerificador1 = resto > 9 ? 0 : resto;
    
    if (digitoVerificador1 !== parseInt(cpf.charAt(9))) return false;
    
    // Validação do segundo dígito verificador
    soma = 0;
    for (let i = 0; i < 10; i++) {
        soma += parseInt(cpf.charAt(i)) * (11 - i);
    }
    resto = 11 - (soma % 11);
    let digitoVerificador2 = resto > 9 ? 0 : resto;
    
    return digitoVerificador2 === parseInt(cpf.charAt(10));
}

// Função para validar CEP
function validarCEP(cep) {
    cep = cep.replace(/\D/g, '');
    return cep.length === 8;
}

// Função para validar telefone
function validarTelefone(telefone) {
    telefone = telefone.replace(/\D/g, '');
    return telefone.length >= 10 && telefone.length <= 11;
}

// Função para validar tamanho e tipo de imagem
function validarImagem(file) {
    const tiposPermitidos = ['image/jpeg', 'image/png', 'image/jpg'];
    const tamanhoMaximo = 5 * 1024 * 1024; // 5MB

    if (!tiposPermitidos.includes(file.type)) {
        throw new Error('Tipo de arquivo não permitido. Use apenas JPG, JPEG ou PNG.');
    }

    if (file.size > tamanhoMaximo) {
        throw new Error('A imagem deve ter no máximo 5MB.');
    }

    return true;
} 