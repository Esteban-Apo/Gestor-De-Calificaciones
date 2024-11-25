document.addEventListener("DOMContentLoaded", () => {
    const cursoId = document.body.dataset.cursoId; // Obtiene el ID del curso desde el atributo del body
    cargarEstudiantes(cursoId);
});

// Función para cargar estudiantes desde el servidor
async function cargarEstudiantes(cursoId) {
    try {
        const respuesta = await fetch(`http://localhost:8080/api/cursos/${cursoId}/estudiantes/detalle`);
        if (!respuesta.ok) {
            throw new Error("Error al obtener los estudiantes del curso");
        }

        const estudiantes = await respuesta.json();
        mostrarEstudiantes(estudiantes, cursoId); // Pasar cursoId para su uso en otras funciones
    } catch (error) {
        console.error("Error al cargar estudiantes:", error);
        Swal.fire({
            icon: "error",
            title: "Error",
            text: "No se pudo cargar la lista de estudiantes.",
        });
    }
}

// Función para mostrar los estudiantes en la lista
function mostrarEstudiantes(estudiantes, cursoId) {
    const contenedor = document.getElementById("contenedor-estudiantes");
    contenedor.innerHTML = ""; // Limpia el contenido previo

    if (estudiantes.length === 0) {
        contenedor.innerHTML = "<p>No hay estudiantes asignados a este curso.</p>";
        return;
    }

    // Crear tabla
    const tabla = document.createElement("table");
    tabla.className = "tabla-estudiantes";

    // Crear encabezado
    const thead = document.createElement("thead");
    thead.innerHTML = `
        <tr>
            <th>Nombre</th>
            <th>Correo</th>
            <th>Acción</th>
        </tr>
    `;
    tabla.appendChild(thead);

    // Crear cuerpo de la tabla
    const tbody = document.createElement("tbody");

    estudiantes.forEach(estudiante => {
        const fila = document.createElement("tr");

        // Columna nombre
        const nombreCelda = document.createElement("td");
        nombreCelda.textContent = estudiante.usuario.nombre;
        fila.appendChild(nombreCelda);

        // Columna correo
        const correoCelda = document.createElement("td");
        correoCelda.textContent = estudiante.usuario.email;
        fila.appendChild(correoCelda);

        // Columna acción (botón eliminar)
        const accionCelda = document.createElement("td");
        const botonEliminar = document.createElement("button");
        botonEliminar.textContent = "Eliminar";
        botonEliminar.className = "btn-eliminar";

        // Asignar evento al botón para eliminar al estudiante
        botonEliminar.onclick = () => eliminarEstudiante(estudiante.id, cursoId);

        accionCelda.appendChild(botonEliminar);
        fila.appendChild(accionCelda);

        tbody.appendChild(fila);
    });

    tabla.appendChild(tbody);
    contenedor.appendChild(tabla);
}

async function eliminarEstudiante(id, cursoId) {
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
            cargarEstudiantes(cursoId); // Recargar la lista después de la eliminación
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
