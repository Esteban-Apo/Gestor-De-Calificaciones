const API_URL = "http://localhost:8080/api";
let userRole = null; // Variable para almacenar el rol del usuario logueado

// Evento para manejar el formulario de agregar estudiantes
document.getElementById("formAgregarEstudiante").addEventListener("submit", async (e) => {
    e.preventDefault();

    const nombreCompleto = document.getElementById("nombreCompleto").value;
    const email = document.getElementById("email").value;
    const contraseña = document.getElementById("contraseña").value;

    try {
        const response = await fetch(`${API_URL}/usuarios/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                nombre: nombreCompleto, // Asegúrate de que coincida con el backend
                email,
                contraseña
            })
        });

        if (!response.ok) {
            const errorText = await response.text(); // Lee la respuesta del backend como texto
            Swal.fire({
                title: "Error al registrar",
                text: errorText, // Muestra el error que envía el backend
                icon: "error",
                confirmButtonText: "Entendido",
            });
            return;
        }

        const usuario = await response.json();
        Swal.fire({
            title: "Usuario registrado",
            text: "El usuario fue registrado exitosamente.",
            icon: "success",
            timer: 2000,
            showConfirmButton: false,
        });

        cargarEstudiantes(); // Recarga la lista de estudiantes
    } catch (error) {
        console.error("Error al registrar el usuario:", error);
        Swal.fire({
            title: "Error al registrar",
            text: "Hubo un problema al registrar el usuario. Por favor, inténtalo de nuevo.",
            icon: "error",
            confirmButtonText: "Entendido",
        });
    }
});

// Función para obtener el rol del usuario logueado
async function obtenerRolUsuario(email) {
    try {
        const response = await fetch(`${API_URL}/usuarios/rol/${email}`);
        if (response.ok) {
            const data = await response.json();
            return data.rol; // Retorna el rol del usuario
        } else {
            console.error("No se pudo obtener el rol del usuario");
            return null;
        }
    } catch (error) {
        console.error("Error al obtener el rol:", error);
        return null;
    }
}

// Función para eliminar un estudiante
async function eliminarEstudiante(id) {
    const result = await Swal.fire({
        title: "¿Estás seguro?",
        text: "Esta acción eliminará al estudiante de forma permanente.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Sí, eliminar",
        cancelButtonText: "Cancelar",
    });

    if (!result.isConfirmed) return;

    try {
        const response = await fetch(`${API_URL}/estudiantes/${id}`, {
            method: "DELETE",
        });

        if (response.ok) {
            Swal.fire({
                title: "¡Eliminado!",
                text: "El estudiante ha sido eliminado exitosamente.",
                icon: "success",
                timer: 2000,
                showConfirmButton: false,
            });
            cargarEstudiantes();
        } else {
            Swal.fire({
                title: "Error",
                text: "No se pudo eliminar el estudiante.",
                icon: "error",
            });
        }
    } catch (error) {
        console.error("Error:", error);
        Swal.fire({
            title: "Error",
            text: "Ocurrió un problema al intentar eliminar el estudiante.",
            icon: "error",
        });
    }
}

// Función para cargar los estudiantes y ajustar la interfaz según el rol
async function cargarEstudiantes() {
    const listaEstudiantes = document.getElementById("listaEstudiantes");
    listaEstudiantes.innerHTML = ""; // Limpiar la lista antes de cargar nuevos datos

    // Obtener el rol del usuario logueado
    const usuarioEmail = "correo_del_usuario_logueado@example.com"; // Reemplaza con el email logueado
    const rolUsuario = await obtenerRolUsuario(usuarioEmail);

    try {
        const response = await fetch(`${API_URL}/estudiantes`);
        const estudiantes = await response.json();

        estudiantes.forEach(estudiante => {
            if (estudiante.usuario) {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${estudiante.usuario.nombre}</td>
                    <td>${estudiante.usuario.email}</td>
                    <td>
                        ${
                            rolUsuario !== "Estudiante"
                                ? `<button class="btn-delete" onclick="eliminarEstudiante(${estudiante.id})">Eliminar</button>`
                                : ""
                        }
                    </td>
                `;
                listaEstudiantes.appendChild(row);
            }
        });

        // Ocultar el formulario de agregar estudiante si el usuario es un estudiante
        if (rolUsuario === "Estudiante") {
            document.getElementById("formAgregarEstudiante").style.display = "none";
        }
    } catch (error) {
        console.error("Error al cargar estudiantes:", error);
    }
}

cargarEstudiantes();  // Cargamos los estudiantes al cargar la página
