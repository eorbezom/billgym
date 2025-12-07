/**
 * Sistema de alertas de autorización para BillGym
 * Coloca este script en: src/main/resources/static/js/authorization-alert.js
 */

// Mostrar alerta cuando se detecta falta de permisos
function mostrarAlertaAutorizacion(mensaje = "Necesitas autorización para realizar esta acción") {
    // Crear contenedor de alerta si no existe
    let alertContainer = document.getElementById('authorization-alert-container');
    
    if (!alertContainer) {
        alertContainer = document.createElement('div');
        alertContainer.id = 'authorization-alert-container';
        alertContainer.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 9999;
            max-width: 400px;
        `;
        document.body.appendChild(alertContainer);
    }
    
    // Crear alerta
    const alert = document.createElement('div');
    alert.className = 'alert alert-warning alert-dismissible fade show shadow-lg';
    alert.style.cssText = `
        border-left: 5px solid #ffc107;
        animation: slideInRight 0.5s ease-out;
    `;
    
    alert.innerHTML = `
        <div class="d-flex align-items-center">
            <i class="bi bi-shield-exclamation fs-3 me-3 text-warning"></i>
            <div>
                <h6 class="alert-heading mb-1">⚠️ Autorización Requerida</h6>
                <p class="mb-0 small">${mensaje}</p>
            </div>
            <button type="button" class="btn-close ms-auto" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    // Agregar animación CSS
    const style = document.createElement('style');
    style.textContent = `
        @keyframes slideInRight {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }
        @keyframes slideOutRight {
            from {
                transform: translateX(0);
                opacity: 1;
            }
            to {
                transform: translateX(100%);
                opacity: 0;
            }
        }
    `;
    if (!document.getElementById('auth-alert-styles')) {
        style.id = 'auth-alert-styles';
        document.head.appendChild(style);
    }
    
    // Agregar alerta al contenedor
    alertContainer.appendChild(alert);
    
    // Auto-cerrar después de 5 segundos
    setTimeout(() => {
        alert.style.animation = 'slideOutRight 0.5s ease-out';
        setTimeout(() => alert.remove(), 500);
    }, 5000);
}

// Interceptar botones que requieren permisos
document.addEventListener('DOMContentLoaded', function() {
    // Agregar data-requires-auth a botones que necesitan autorización
    const restrictedButtons = document.querySelectorAll('[data-requires-auth]');
    
    restrictedButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            const requiredRole = this.getAttribute('data-required-role');
            const userRole = this.getAttribute('data-user-role');
            
            if (requiredRole && userRole && !userRole.includes(requiredRole)) {
                e.preventDefault();
                mostrarAlertaAutorizacion(
                    `Esta acción requiere el rol: ${requiredRole}. Tu rol actual no tiene permisos suficientes.`
                );
            }
        });
    });
});

// Detectar errores 403 en peticiones AJAX
document.addEventListener('DOMContentLoaded', function() {
    // Interceptar fetch
    const originalFetch = window.fetch;
    window.fetch = function(...args) {
        return originalFetch.apply(this, args).then(response => {
            if (response.status === 403) {
                mostrarAlertaAutorizacion('No tienes autorización para acceder a este recurso');
            }
            return response;
        });
    };
});

// Mostrar alerta si hay parámetro de error en URL
window.addEventListener('load', function() {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('unauthorized') === 'true') {
        mostrarAlertaAutorizacion('No tienes permisos para realizar esa acción');
    }
});