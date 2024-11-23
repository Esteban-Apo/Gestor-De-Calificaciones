// URL de la API
const API_URL = 'http://localhost:8080/api'; // Ajusta la ruta según la ruta de tu backend

// Elementos del DOM
const dashboard = document.getElementById('dashboard');
const loginSection = document.getElementById('login');
const adminSection = document.getElementById("adminSection");

document.addEventListener('DOMContentLoaded', async function() {

    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    
    // Manejar el login
    if (loginForm) {
        loginForm.addEventListener('submit', async (event) => {
            event.preventDefault();

            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            
            const response = await fetch(`http://localhost:8080/api/usuarios/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, contraseña: password })
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('userRole', data.rol);  // Guardar rol en localStorage
                window.location.href = 'perfil.html';  // Redireccionar a la página principal
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Credenciales incorrectas, nombre de usuario o contraseña no válidos',
                    confirmButtonColor: '#d33',
                    confirmButtonText: 'Cerrar'
                });
            }
        });
    }

    // Registro de usuarios
if (registerForm) {
    registerForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const username = document.getElementById('username').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        // Expresión regular para validar la contraseña
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
        if (!passwordRegex.test(password)) {
            // Usar SweetAlert2 para mostrar un mensaje agradable
            Swal.fire({
                icon: 'error',
                title: 'Contraseña no válida',
                text: 'La contraseña debe tener mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.',
                confirmButtonText: 'Entendido',
                timer: 5000, // La alerta desaparecerá automáticamente después de 5 segundos
                timerProgressBar: true
            });
            return;
        }

        try {
            const response = await fetch(`${API_URL}/usuarios/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ nombre: username, email, contraseña: password })
            });

            if (response.ok) {
                //SweetAlert2
                Swal.fire({
                    icon: 'success',
                    title: 'Registro exitoso',
                    text: 'Tu cuenta ha sido creada correctamente.',
                    confirmButtonText: 'Ir al perfil'
                }).then(() => {
                    // Redirigir al perfil después de cerrar el modal
                    localStorage.setItem('userRole', 'Estudiante');
                    window.location.href = '../views/perfil.html';
                });
            } else {
                //SweetAlert2
                Swal.fire({
                    icon: 'error',
                    title: 'Correo duplicado',
                    text: 'El correo ya está registrado, ingrese otro correo.',
                    confirmButtonText: 'Intentar de nuevo'
                });
            }
        } catch (error) {
            //SweetAlert2
            Swal.fire({
                icon: 'error',
                title: 'Error de conexión',
                text: 'No se pudo conectar al servidor. Intenta nuevamente.',
                confirmButtonText: 'Entendido'
            });
        }
    });
}


    // Llamar a la función de verificación de rol
    await verificarRol();

});

