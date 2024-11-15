document.addEventListener("DOMContentLoaded", () => {
    const profesorSelect = document.getElementById("profesorSelect");
    const materiaSelect = document.getElementById("materiaSelect");
    const asignarMateriaForm = document.getElementById("asignarMateriaForm");

    // Cargar los profesores
    fetch("http://localhost:8080/api/profesores")
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(profesores => {
        const profesorSelect = document.getElementById("profesorSelect"); // Asegúrate de que el id coincide con el dropdown en el HTML
        profesores.forEach(profesor => {
            const option = document.createElement("option");
            option.value = profesor.id;
            option.textContent = profesor.nombreCompleto; // O el campo correspondiente al nombre completo del profesor
            profesorSelect.appendChild(option);
        });
    })
    .catch(error => console.error("Error al cargar profesores:", error));


    // Cargar las materias
    fetch("http://localhost:8080/api/materias")
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(materias => {
        console.log(materias); // Agrega esto para ver la respuesta
        if (Array.isArray(materias)) {
            materias.forEach(materia => {
                const option = document.createElement("option");
                option.value = materia.id;
                option.textContent = materia.nombre;
                materiaSelect.appendChild(option);
            });
        } else {
            console.error("La respuesta de materias no es un array:", materias);
        }
    })
    .catch(error => console.error("Error al cargar materias:", error));

    // Manejar el envío del formulario para asignar la materia al profesor
    asignarMateriaForm.addEventListener("submit", (event) => {
        event.preventDefault();

        const profesorId = profesorSelect.value;
        const materiaId = materiaSelect.value;

        fetch(`http://localhost:8080/api/profesores/${profesorId}/materias`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify( materiaId)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => console.log("Materia asignada exitosamente:", data))
        .catch(error => console.error("Error al asignar materia:", error));
    });
});
