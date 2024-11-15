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
                alert("Credenciales incorrectas, nombre de usuario o contraseña no válidos");
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

            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
            if (!passwordRegex.test(password)) {
                alert('La contraseña debe tener mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.');
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
                    localStorage.setItem('userRole', 'Estudiante'); 
                    alert('Registro exitoso.');
                    window.location.href = '../views/perfil.html';
                } else {
                    alert('El correo ya está registrado, ingrese otro correo');
                }
            } catch (error) {
                alert('Error de conexión. Intenta nuevamente.');
            }
        });
    }


    // Llamar a la función de verificación de rol
    await verificarRol();

});

