document.addEventListener("DOMContentLoaded", function () {
    const cursoId = document.body.dataset.cursoId;
    const formAgregarMateria = document.getElementById("formAgregarMateria");
    const inputNombreMateria = document.getElementById("nombreMateria");

    // Cargar las materias del curso
    function cargarMaterias() {
        fetch(`http://localhost:8080/api/materias/curso/${cursoId}`)
            .then(response => response.json())
            .then(materias => {
                const listaMaterias = document.getElementById("materias-list");
                listaMaterias.innerHTML = ""; // Limpiar contenido previo

                materias.forEach(materia => {
                    // Crear elemento de la lista
                    const itemMateria = document.createElement("li");
                    itemMateria.textContent = materia.nombre;

                    // Botón para ingresar calificaciones
                    const botonCalificaciones = document.createElement("button");
                    botonCalificaciones.textContent = "Ingresar Calificaciones";
                    botonCalificaciones.className = "btn-calificaciones";
                    botonCalificaciones.onclick = () => abrirVistaCalificaciones(materia.id);

                    // Añadir botón al elemento
                    itemMateria.appendChild(botonCalificaciones);
                    listaMaterias.appendChild(itemMateria);
                });
            })
            .catch(error => console.error("Error al cargar materias:", error));
    }

    // Redirigir a la vista de calificaciones
    function abrirVistaCalificaciones(materiaId) {
        // Obtén el cursoId del atributo dataset del body
        const cursoId = document.body.dataset.cursoId;
        // Incluye cursoId y materiaId en la URL
        window.location.href = `../calificaciones.html?cursoId=${cursoId}&materiaId=${materiaId}`;
    }

    // Agregar nueva materia
    formAgregarMateria.addEventListener("submit", (event) => {
        event.preventDefault();

        const materia = { 
            nombre: inputNombreMateria.value,
            profesorId: null 
        };

        fetch(`http://localhost:8080/api/materias/curso/${cursoId}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(materia)
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    throw new Error(errorData.message || "Error al procesar la solicitud.");
                });
            }
            return response.json();
        })
        .then(data => {
            Swal.fire({
                icon: "success",
                title: "Materia agregada",
                text: data.message || "La materia se ha agregado exitosamente.",
                confirmButtonText: "Aceptar"
            }).then(() => {
                location.reload();
            });
        })
        .catch(error => {
            console.error("Error al agregar materia:", error);

            Swal.fire({
                icon: "error",
                title: "Error al agregar materia",
                text: error.message || "Ocurrió un error al intentar agregar la materia. Intenta nuevamente.",
                confirmButtonText: "Entendido"
            });
        });
    });

    // Cargar las materias al iniciar la página
    cargarMaterias();
});
