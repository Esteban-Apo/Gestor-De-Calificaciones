document.addEventListener("DOMContentLoaded", function () {
    const cursoId = document.body.dataset.cursoId; // Suponiendo que se pasa el ID del curso en el HTML
    const materiasList = document.getElementById("materias-list");
    const formAgregarMateria = document.getElementById("formAgregarMateria");
    const inputNombreMateria = document.getElementById("nombreMateria");

    // Función para mostrar las materias en la lista
    function mostrarMateria(materia) {
        const materiaItem = document.createElement("li");
        materiaItem.textContent = materia.nombre;
        materiasList.appendChild(materiaItem);
    }

    // Cargar las materias del curso
    function cargarMaterias() {
        fetch(`http://localhost:8080/api/materias/curso/${cursoId}`)
            .then(response => response.json())
            .then(materias => {
                materias.forEach(mostrarMateria);
            })
            .catch(error => console.error("Error al cargar materias:", error));
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
                // Si hay un error HTTP, lanza una excepción
                return response.json().then(errorData => {
                    throw new Error(errorData.message || "Error al procesar la solicitud.");
                });
            }
            return response.json(); // Procesar como JSON si la respuesta es exitosa
        })
        .then(data => {
            // Muestra un mensaje de éxito con SweetAlert2
            Swal.fire({
                icon: "success",
                title: "Materia agregada",
                text: data.message || "La materia se ha agregado exitosamente.",
                confirmButtonText: "Aceptar"
            }).then(() => {
                // Opcional: recarga la página o actualiza la lista de materias
                location.reload(); // Actualiza la página para reflejar los cambios
            });
        })
        .catch(error => {
            console.error("Error al agregar materia:", error);
    
            // Muestra un mensaje de error con SweetAlert2
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
