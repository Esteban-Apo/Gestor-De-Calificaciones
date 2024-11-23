/*document.addEventListener("DOMContentLoaded", () => {
    fetch("http://localhost:8080/api/usuarios/estudiantes")
        .then(response => response.json())
        .then(estudiantes => {
            const estudiantesList = document.getElementById("estudiantesList");
            estudiantes.forEach(estudiante => {
                const listItem = document.createElement("li");
                listItem.textContent = `${estudiante.nombre} - ${estudiante.email}`;
                estudiantesList.appendChild(listItem);
            });
        })
        .catch(error => console.error("Error al cargar estudiantes:", error));
}); No se elimina para verificar si se necesita*/

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
                nombre: nombreCompleto,email , contraseña})
        });
    
        if (!response.ok) {
            const errorData = await response.json(); // Ahora espera un JSON
            alert(`Error: ${errorData.error}`); // Usa el mensaje de error del backend
            return;
        }
    
        const usuario = await response.json();
        alert("Usuario registrado exitosamente");
    } catch (error) {
        console.error("Error al registrar el usuario:", error);
        alert("Error al registrar el usuario. Por favor, inténtalo de nuevo.");
    }
    
});

// Método para eliminar un estudiante
async function eliminarEstudiante(id) {
    const confirmacion = confirm("¿Estás seguro de que deseas eliminar este estudiante?");
    if (!confirmacion) return;

    try {
        const response = await fetch(`${API_URL}/estudiantes/${id}`, {
            method: "DELETE",
        });

        if (response.ok) {
            alert("Estudiante eliminado exitosamente.");
            cargarEstudiantes();  // Recargar la lista después de la eliminación
        } else {
            alert("Error al eliminar el estudiante.");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("No se pudo eliminar el estudiante.");
    }
}

// Método para cargar la lista de estudiantes
async function cargarEstudiantes() {
    const listaEstudiantes = document.getElementById("listaEstudiantes");
    listaEstudiantes.innerHTML = "";

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



cargarEstudiantes();

