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
    
        const materia = { nombre: inputNombreMateria.value };
    
        fetch(`http://localhost:8080/api/materias/curso/${cursoId}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(materia)
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            return response.json();
        })
        .then(data => {
            mostrarMateria(data);
            formAgregarMateria.reset();
        })
        .catch(error => console.error("Error al agregar materia:", error));
    });

    // Cargar las materias al iniciar la página
    cargarMaterias();
});
