// Classe para gerenciar os serviços
class ServiceManager {
    constructor() {
        this.services = this.loadServices();
        this.currentProvider = this.loadCurrentProvider();
    }

    // Carrega os serviços do localStorage
    loadServices() {
        const services = localStorage.getItem('services');
        return services ? JSON.parse(services) : [];
    }

    // Carrega o prestador atual do localStorage
    loadCurrentProvider() {
        const provider = localStorage.getItem('currentProvider');
        return provider ? JSON.parse(provider) : null;
    }

    // Adiciona um novo serviço
    addService(service) {
        service.id = Date.now().toString();
        service.status = 'pendente';
        service.createdAt = new Date().toISOString();
        this.services.push(service);
        this.saveServices();
        return service;
    }

    // Obtém todos os serviços
    getAllServices() {
        return this.loadServices(); // Sempre retorna os dados mais recentes
    }

    // Obtém serviços por categoria
    getServicesByCategory(category) {
        const services = this.loadServices();
        return services.filter(service => 
            isCategoryMatch(category, service.category)
        );
    }

    // Obtém serviços para o prestador atual
    getServicesForCurrentProvider() {
        if (!this.currentProvider) return [];
        const services = this.loadServices();
        return services.filter(service => 
            isCategoryMatch(this.currentProvider.serviceType, service.category)
        );
    }

    // Atualiza o status de um serviço
    updateServiceStatus(serviceId, status) {
        const services = this.loadServices();
        const serviceIndex = services.findIndex(s => s.id === serviceId);
        
        if (serviceIndex !== -1) {
            services[serviceIndex].status = status;
            localStorage.setItem('services', JSON.stringify(services));
            this.services = services; // Atualiza a instância atual
            return true;
        }
        return false;
    }

    // Salva os serviços no localStorage
    saveServices() {
        localStorage.setItem('services', JSON.stringify(this.services));
    }
}

// Inicializa o gerenciador de serviços
const serviceManager = new ServiceManager();

// Função para criar um card de serviço
function createServiceCard(service, onAction) {
    const card = document.createElement('div');
    card.className = 'col-lg-4 col-md-6';
    card.innerHTML = `
      <div class="service-card custom-service-card">
        <div class="service-card-header d-flex justify-content-between align-items-start">
          <h5 class="service-client mb-0">Solicitado por <span>${service.nome || 'Cliente'}</span></h5>
          <span class="service-category">${service.habilidadeRequisitada ? service.habilidadeRequisitada.charAt(0).toUpperCase() + service.habilidadeRequisitada.slice(1) : ''}</span>
        </div>
        ${service.imagem ? `
        <div class="service-image-container" style="width: 100%; height: 200px; overflow: hidden; margin: 1rem 0; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); border: 1px solid rgba(0,0,0,0.1); cursor: pointer;" onclick="openImageModal('${service.imagem}')">
          <img src="${service.imagem}" alt="Imagem do serviço" style="width: 100%; height: 100%; object-fit: cover; transition: transform 0.3s ease;">
        </div>
        ` : ''}
        <div class="service-card-body">
          <div class="service-description">${service.descricao || 'Sem descrição'}</div>
          <div class="service-info mt-3">
            <div class="service-info-item"><i class="bi bi-geo-alt"></i> ${service.enderecoCliente || 'Não informado'}</div>
            <div class="service-info-item"><i class="bi bi-telephone"></i> ${service.telefoneContato || 'Não informado'}</div>
            <div class="service-info-item"><i class="bi bi-clock"></i> ${service.disponibilidade || 'Não informado'}</div>
            <div class="service-info-item"><i class="bi bi-currency-dollar"></i> R$ ${service.valor || 'Não informado'}</div>
          </div>
        </div>
        <div class="service-card-footer">
          <a href="#" class="service-link" onclick="aceitarServico('${service.idPedido}'); return false;">Aceitar</a>
          <a href="#" class="service-link text-danger ms-3" onclick="rejeitarServico('${service.idPedido}'); return false;">Recusar</a>
        </div>
      </div>
    `;
    return card;
}

// Função para abrir o modal com a imagem
function openImageModal(imageSrc) {
    let modal = document.getElementById('imageModal');
    if (!modal) {
        modal = document.createElement('div');
        modal.id = 'imageModal';
        modal.style.cssText = `
            display: none;
            position: fixed;
            z-index: 2000;
            left: 0;
            top: 0;
            width: 100vw;
            height: 100vh;
            background-color: rgba(0,0,0,0.9);
            overflow: auto;
        `;
        modal.innerHTML = `
            <div style="position: relative; width: 100vw; height: 100vh; display: flex; justify-content: center; align-items: center;">
                <img id="modalImage" style="max-width: 90vw; max-height: 90vh; object-fit: contain;">
                <span class="close-modal" style="position: absolute; top: 24px; right: 48px; color: #f1f1f1; font-size: 40px; font-weight: bold; cursor: pointer; z-index: 2100; background: rgba(0,0,0,0.3); border-radius: 50%; padding: 0 16px;">&times;</span>
            </div>
        `;
        document.body.appendChild(modal);
        modal.querySelector('.close-modal').onclick = function() {
            modal.style.display = "none";
        };
        // Fecha o modal ao clicar fora da imagem
        modal.onclick = function(event) {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        };
    }
    document.getElementById('modalImage').src = imageSrc;
    modal.style.display = "block";
}

async function fetchServicos() {
    const res = await fetch('http://localhost:8081/pedido', { credentials: 'include' });
    if (!res.ok) throw new Error('Erro ao buscar serviços');
    return await res.json();
}

async function aceitarServico(idPedido) {
    try {
        const res = await fetch(`http://localhost:8081/pedido/${idPedido}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({ status: 'aceito' })
        });
        if (!res.ok) throw new Error('Erro ao aceitar serviço');
        alert('Serviço aceito com sucesso!');
        loadServicesFromBackend();
    } catch (e) {
        alert('Erro ao aceitar serviço.');
    }
}

async function rejeitarServico(idPedido) {
    try {
        const res = await fetch(`http://localhost:8081/pedido/${idPedido}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({ status: 'rejected' })
        });
        if (!res.ok) throw new Error('Erro ao recusar serviço');
        alert('Serviço recusado!');
        loadServicesFromBackend();
    } catch (e) {
        alert('Erro ao recusar serviço.');
    }
}

// Função para marcar serviço como concluído
async function marcarComoConcluido(idPedido) {
    try {
        const res = await fetch(`http://localhost:8081/pedido/${idPedido}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({ status: 'concluido' })
        });
        if (!res.ok) throw new Error('Erro ao marcar como concluído');
        alert('Serviço marcado como concluído!');
        loadAcceptedServicesForDashboard();
        atualizarContadoresDashboard();
    } catch (e) {
        alert('Erro ao marcar como concluído.');
    }
}

// Atualiza os contadores da dashboard
async function atualizarContadoresDashboard() {
    let user = await getLoggedUser();
    if (!user || user.tipo !== 'Prestador') return;
    let habilidadePrestador = (user.habilidade || '').trim().toLowerCase();
    try {
        let pedidos = await fetchServicos();
        // Aceitos: todos com status 'aceito' ou 'concluido'
        let aceitos = pedidos.filter(p => (p.status === 'aceito' || p.status === 'concluido') && (p.habilidadeRequisitada || '').trim().toLowerCase() === habilidadePrestador).length;
        // Pendentes: apenas os aceitos que ainda não foram marcados como concluídos
        let pendentes = pedidos.filter(p => p.status === 'aceito' && (p.habilidadeRequisitada || '').trim().toLowerCase() === habilidadePrestador).length;
        // Concluídos: status 'concluido'
        let concluidos = pedidos.filter(p => p.status === 'concluido' && (p.habilidadeRequisitada || '').trim().toLowerCase() === habilidadePrestador).length;
        document.getElementById('pendenteServicesCount').textContent = pendentes;
        document.getElementById('aceitoServicesCount').textContent = aceitos;
        document.getElementById('completedServicesCount').textContent = concluidos;
    } catch (e) {}
}

// Atualizar loadServicesFromBackend para exibir apenas pendentes em academics.html
async function loadServicesFromBackend() {
    const allServicesContainer = document.getElementById('allServices');
    const categoryServicesContainer = document.getElementById('categoryServices');
    const otherServicesContainer = document.getElementById('otherServices');
    if (allServicesContainer) allServicesContainer.innerHTML = '';
    if (categoryServicesContainer) categoryServicesContainer.innerHTML = '';
    if (otherServicesContainer) otherServicesContainer.innerHTML = '';

    let user = await getLoggedUser();
    if (!user || user.tipo !== 'Prestador') {
        window.location.href = 'login.html';
        return;
    }
    let categoriaPrestador = user.habilidade || user.categoria || user.serviceType || '';
    try {
        let pedidos = await fetchServicos();
        // Exibe apenas pedidos pendentes
        pedidos.filter(p => p.status === 'pendente').forEach(pedido => {
            if (allServicesContainer) allServicesContainer.appendChild(createServiceCard(pedido));
        });
        // Exibe pedidos pendentes da categoria do prestador
        pedidos.filter(p => p.status === 'pendente' && p.habilidadeRequisitada === categoriaPrestador).forEach(pedido => {
            if (categoryServicesContainer) categoryServicesContainer.appendChild(createServiceCard(pedido));
        });
        // Exibe pedidos pendentes de outras categorias
        pedidos.filter(p => p.status === 'pendente' && p.habilidadeRequisitada !== categoriaPrestador).forEach(pedido => {
            if (otherServicesContainer) otherServicesContainer.appendChild(createServiceCard(pedido));
        });
    } catch (e) {
        if (allServicesContainer) allServicesContainer.innerHTML = '<p class="text-danger">Erro ao carregar serviços.</p>';
        if (categoryServicesContainer) categoryServicesContainer.innerHTML = '';
        if (otherServicesContainer) otherServicesContainer.innerHTML = '';
    }
}

window.aceitarServico = aceitarServico;
window.rejeitarServico = rejeitarServico;
window.loadServicesFromBackend = loadServicesFromBackend;

// Função para atualizar o dashboard
function updateDashboard() {
    const provider = serviceManager.currentProvider;
    
    if (provider && provider.serviceType) {
        // Obtém todos os serviços atualizados
        const allServices = serviceManager.getAllServices();
        
        // Filtra serviços por categoria do prestador
        const providerServices = allServices.filter(s => 
            isCategoryMatch(provider.serviceType, s.category)
        );

        // Atualiza contadores
        const pendingCount = providerServices.filter(s => s.status === 'pendente').length;
        const acceptedCount = providerServices.filter(s => s.status === 'aceito').length;
        const completedCount = providerServices.filter(s => s.status === 'completed').length;

        document.getElementById('pendingServicesCount').textContent = pendingCount;
        document.getElementById('acceptedServicesCount').textContent = acceptedCount;
        document.getElementById('completedServicesCount').textContent = completedCount;

        // Exibe serviços aceitos
        const acceptedServices = providerServices.filter(s => s.status === 'aceito');
        const container = document.getElementById('acceptedServicesContainer');
        
        if (acceptedServices.length === 0) {
            container.innerHTML = '<p class="text-muted">Nenhum serviço aceito no momento.</p>';
        } else {
            container.innerHTML = acceptedServices.map(service => `
                <div class="dashboard-next-service mb-4 p-3 rounded" style="background:#e3f2fd; box-shadow:0 2px 8px rgba(13,110,253,0.06);">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <span class="badge bg-primary">${service.category ? service.category.charAt(0).toUpperCase() + service.category.slice(1) : ''}</span>
                        <span class="badge bg-success">Aceito</span>
                    </div>
                    <div><b>Cliente:</b> ${service.clientName || '-'}</div>
                    <div><b>Endereço:</b> ${service.address || '-'}</div>
                    <div><b>Horário:</b> ${service.availability || '-'}</div>
                    <div><b>Orçamento:</b> R$ ${service.budget || '-'}</div>
                    <div><b>Descrição:</b> ${service.description || '-'}</div>
                    <div class="mt-3">
                        <button class="btn btn-success btn-sm" onclick="handleServiceAction('${service.id}', 'completed')">
                            <i class="bi bi-check-circle"></i> Marcar como Concluído
                        </button>
                    </div>
                </div>
            `).join('');
        }
    }
}

// Função para carregar os serviços
function loadServices() {
    const allServicesContainer = document.getElementById('allServices');
    const categoryServicesContainer = document.getElementById('categoryServices');
    const otherServicesContainer = document.getElementById('otherServices');

    if (allServicesContainer) {
        allServicesContainer.innerHTML = '';
        // Exibe apenas serviços pendentes
        const services = serviceManager.getAllServices().filter(s => s.status === 'pendente');
        services.forEach(service => {
            allServicesContainer.appendChild(createServiceCard(service));
        });
    }

    if (categoryServicesContainer) {
        categoryServicesContainer.innerHTML = '';
        // Exibe apenas serviços pendentes da categoria do prestador
        const services = serviceManager.getServicesForCurrentProvider().filter(s => s.status === 'pendente');
        services.forEach(service => {
            categoryServicesContainer.appendChild(createServiceCard(service));
        });
    }

    if (otherServicesContainer) {
        otherServicesContainer.innerHTML = '';
        // Exibe apenas serviços pendentes de outras categorias
        const services = serviceManager.getAllServices().filter(service => 
            !isCategoryMatch(serviceManager.currentProvider?.serviceType, service.category) && service.status === 'pendente'
        );
        services.forEach(service => {
            otherServicesContainer.appendChild(createServiceCard(service));
        });
    }
}

// Carrega os serviços ao iniciar a página
document.addEventListener('DOMContentLoaded', () => {
    loadServices();
    // Se estiver na página do dashboard, atualiza o dashboard
    if (window.location.pathname.includes('provider-dashboard.html')) {
        updateDashboard();
    }
    // Adicione o CSS customizado para o novo visual dos cards
    if (!document.getElementById('custom-service-card-style')) {
        const style = document.createElement('style');
        style.id = 'custom-service-card-style';
        style.innerHTML = `
          .custom-service-card {
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 2px 15px rgba(0,0,0,0.07);
            padding: 24px 20px 16px 20px;
            margin-bottom: 24px;
            min-height: 320px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
          }
          .custom-service-card .service-card-header {
            width: 100%;
            margin-bottom: 8px;
          }
          .custom-service-card .service-icon {
            font-size: 2rem;
            color: #222;
          }
          .custom-service-card .service-category {
            font-size: 1rem;
            color: #6c757d;
            font-weight: 600;
            margin-top: 2px;
          }
          .custom-service-card .service-client {
            color: #6c3a5c;
            font-size: 1.15rem;
            font-weight: 500;
            margin-bottom: 4px;
          }
          .custom-service-card .service-client span {
            color: #6c3a5c;
            font-weight: 600;
          }
          .custom-service-card .service-description {
            color: #444;
            font-size: 1rem;
            margin-bottom: 8px;
          }
          .custom-service-card .service-info {
            font-size: 0.98rem;
            color: #555;
          }
          .custom-service-card .service-info-item {
            margin-bottom: 4px;
            display: flex;
            align-items: center;
            gap: 6px;
          }
          .custom-service-card .service-card-footer {
            border-top: 1px solid #eee;
            margin-top: 16px;
            padding-top: 10px;
            display: flex;
            align-items: center;
          }
          .custom-service-card .service-link {
            color: #1a237e;
            font-weight: 600;
            text-decoration: none;
            font-size: 1rem;
            transition: color 0.2s;
          }
          .custom-service-card .service-link:hover {
            color: #0d47a1;
            text-decoration: underline;
          }
          .custom-service-card .service-link.text-danger {
            color: #b71c1c;
          }
          .custom-service-card .service-link.text-danger:hover {
            color: #d32f2f;
          }
        `;
        document.head.appendChild(style);
    }
});

// Função para carregar serviços aceitos para o dashboard do prestador
async function loadAcceptedServicesForDashboard() {
    const container = document.getElementById('aceitoServicesContainer');
    if (!container) return;
    container.innerHTML = '<p class="text-muted">Carregando...</p>';
    let user = await getLoggedUser();
    if (!user || user.tipo !== 'Prestador') {
        container.innerHTML = '<p class="text-danger">Usuário não autenticado.</p>';
        return;
    }
    let habilidadePrestador = (user.habilidade || '').trim().toLowerCase();
    try {
        let pedidos = await fetchServicos();
        // Exibe todos os aceitos ou concluídos
        let aceitos = pedidos.filter(p => (p.status === 'aceito' || p.status === 'concluido') && (p.habilidadeRequisitada || '').trim().toLowerCase() === habilidadePrestador);
        if (aceitos.length === 0) {
            container.innerHTML = '<p class="text-muted">Nenhum serviço aceito no momento.</p>';
        } else {
            container.innerHTML = aceitos.map(service => `
                <div class="dashboard-next-service mb-4 p-3 rounded d-flex flex-row gap-3 align-items-start" style="background:#e3f2fd; box-shadow:0 2px 8px rgba(13,110,253,0.06); min-height: 200px;">
                    ${service.imagem ? `
                    <div class="service-image-container" style="width: 200px; height: 200px; overflow: hidden; border-radius: 12px; box-shadow: 0 2px 8px rgba(44,62,80,0.10); border: 1px solid #eee; cursor: pointer;" onclick="openImageModal('${service.imagem}')">
                        <img src="${service.imagem}" alt="Imagem do serviço" style="width: 100%; height: 100%; object-fit: cover; transition: transform 0.3s ease;">
                    </div>
                    ` : ''}
                    <div style="flex: 1; min-width: 0;">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <span class="badge bg-primary">${service.habilidadeRequisitada ? service.habilidadeRequisitada.charAt(0).toUpperCase() + service.habilidadeRequisitada.slice(1) : ''}</span>
                            <span class="badge ${service.status === 'concluido' ? 'bg-secondary' : 'bg-success'}">${service.status === 'concluido' ? 'Concluído' : 'Aceito'}</span>
                        </div>
                        <div><b>Cliente:</b> ${service.nome || '-'}</div>
                        <div><b>Endereço:</b> ${service.enderecoCliente || '-'}</div>
                        <div><b>Horário:</b> ${service.disponibilidade || '-'}</div>
                        <div><b>Orçamento:</b> R$ ${service.valor || '-'}</div>
                        <div><b>Descrição:</b> ${service.descricao || '-'}</div>
                        <div class="mt-3">
                            ${service.status === 'aceito' ? `<button class="btn btn-success btn-sm" onclick="marcarComoConcluido('${service.idPedido}')"><i class="bi bi-check-circle"></i> Marcar como Concluído</button>` : `<span class='text-secondary'><i class=\"bi bi-check-circle\"></i> Serviço já concluído</span>`}
                        </div>
                    </div>
                </div>
            `).join('');
        }
        atualizarContadoresDashboard();
    } catch (e) {
        container.innerHTML = '<p class="text-danger">Erro ao carregar serviços aceitos.</p>';
    }
} 