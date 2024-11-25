const API_URL = "http://localhost:8080/api";

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
                title: 'Error al registrar',
                text: errorText, // Muestra el error que envía el backend
                icon: 'error',
                confirmButtonText: 'Entendido'
            });
            return;
        }

        const usuario = await response.json();
        Swal.fire({
            title: 'Usuario registrado',
            text: 'El usuario fue registrado exitosamente.',
            icon: 'success',
            timer: 2000,
            showConfirmButton: false
        });

        cargarEstudiantes(); // Recarga la lista de estudiantes
    } catch (error) {
        console.error("Error al registrar el usuario:", error);
        Swal.fire({
            title: 'Error al registrar',
            text: 'Hubo un problema al registrar el usuario. Por favor, inténtalo de nuevo.',
            icon: 'error',
            confirmButtonText: 'Entendido'
        });
    }
});


async function eliminarEstudiante(id) {
    // Mostrar la alerta de confirmación
    const result = await Swal.fire({
        title: '¿Estás seguro?',
        text: 'Esta acción eliminará al estudiante de forma permanente.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar',
    });

    // Si el usuario cancela, no hacemos nada
    if (!result.isConfirmed) return;

    try {
        const response = await fetch(`http://localhost:8080/api/estudiantes/${id}`, {
            method: 'DELETE',
        });

        if (response.ok) {
            // Mostrar alerta de éxito
            Swal.fire({
                title: '¡Eliminado!',
                text: 'El estudiante ha sido eliminado exitosamente.',
                icon: 'success',
                timer: 2000,
                showConfirmButton: false,
            });
            cargarEstudiantes(); // Recargar la lista después de la eliminación
        } else {
            // Mostrar alerta de error
            Swal.fire({
                title: 'Error',
                text: 'No se pudo eliminar el estudiante.',
                icon: 'error',
            });
        }
    } catch (error) {
        console.error('Error:', error);
        Swal.fire({
            title: 'Error',
            text: 'Ocurrió un problema al intentar eliminar el estudiante.',
            icon: 'error',
        });
    }
}

async function cargarEstudiantes() {
    const listaEstudiantes = document.getElementById("listaEstudiantes");
    listaEstudiantes.innerHTML = ""; // Limpiar la lista antes de cargar nuevos datos

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
                        <button class="btn-delete" onclick="eliminarEstudiante(${estudiante.id})">Eliminar</button>
                    </td>
                `;
                listaEstudiantes.appendChild(row);
            }
        });
    } catch (error) {
        console.error("Error al cargar estudiantes:", error);
    }
}

cargarEstudiantes();  // Cargar estudiantes al iniciar

