document.addEventListener("DOMContentLoaded", function () {
    const btnAbrirModal = document.getElementById("btnAbrirModal");
    const modalAgregarProfesor = document.getElementById("modalAgregarProfesor");
    const btnCerrarModal = document.getElementById("btnCerrarModal");
    const formAgregarProfesor = document.getElementById("formAgregarProfesor");
    const profesoresTableBody = document.getElementById("profesoresTableBody"); // Corregido el ID de la tabla

    // Función para cargar profesores con materias y cursos
    function cargarProfesoresConMaterias() {
        fetch("http://localhost:8080/api/profesores/conMaterias")
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(profesores => {
                profesoresTableBody.innerHTML = ""; // Limpiar la tabla antes de cargar
    
                profesores.forEach(profesor => {
                    const row = document.createElement("tr");
    
                    const nombreCell = document.createElement("td");
                    nombreCell.textContent = profesor.nombreCompleto;
                    row.appendChild(nombreCell);
    
                    const emailCell = document.createElement("td");
                    emailCell.textContent = profesor.email;
                    row.appendChild(emailCell);
    
                    const celularCell = document.createElement("td");
                    celularCell.textContent = profesor.celular;
                    row.appendChild(celularCell);
    
                    const materiasCell = document.createElement("td");
                    materiasCell.textContent = profesor.materias.length > 0
                        ? profesor.materias.map(materia => `${materia.nombre} (${materia.curso})`).join(", ")
                        : "Sin materias asignadas";
                    row.appendChild(materiasCell);
    
                    profesoresTableBody.appendChild(row);
                });
            })
            .catch(error => {
                console.error("Error al cargar profesores con materias:", error);
                alert("Ocurrió un error al cargar los datos. Revisa la consola para más detalles.");
            });
    }

    // Abrir el modal al hacer clic en "Agregar Profesor"
    btnAbrirModal.addEventListener("click", () => {
        modalAgregarProfesor.style.display = "block";
    });

    // Cerrar el modal al hacer clic en la "X"
    btnCerrarModal.addEventListener("click", () => {
        modalAgregarProfesor.style.display = "none";
    });

    // Cerrar el modal al hacer clic fuera del modal
    window.addEventListener("click", (event) => {
        if (event.target === modalAgregarProfesor) {
            modalAgregarProfesor.style.display = "none";
        }
    });

    // Agregar profesor al backend
    formAgregarProfesor.addEventListener("submit", (event) => {
        event.preventDefault();

        const nombre = document.getElementById("nombre").value.trim();
        const email = document.getElementById("email").value.trim();
        const celular = document.getElementById("celular").value.trim();

        // Validar datos básicos
        if (!nombre || !email || !celular) {
            alert("Por favor, completa todos los campos antes de enviar.");
            return;
        }

        // Crear objeto profesor
        const profesor = { nombreCompleto: nombre, email, celular };

        // Enviar datos al backend
        fetch("http://localhost:8080/api/profesores/agregar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(profesor)
        })
            .then(response => {
                if (response.status === 409) {
                    throw new Error("El correo electrónico ya está en uso. Por favor, intenta con uno diferente.");
                } else if (!response.ok) {
                    throw new Error("Error desconocido al agregar el profesor.");
                }
                return response.json();
            })
            .then(data => {
                // Mostrar alerta de éxito
                Swal.fire({
                    icon: "success",
                    title: "Éxito",
                    text: "Profesor agregado exitosamente.",
                    confirmButtonColor: "#3085d6",
                    confirmButtonText: "Cerrar"
                });

                // Actualizar la tabla de profesores
                cargarProfesoresConMaterias();

                // Limpiar el formulario y cerrar el modal
                formAgregarProfesor.reset();
                modalAgregarProfesor.style.display = "none";
            })
            .catch(error => {
                console.error("Error al agregar profesor:", error);
                Swal.fire({
                    icon: "error",
                    title: "Error",
                    text: error.message,
                    confirmButtonColor: "#d33",
                    confirmButtonText: "Cerrar"
                });
            });
    });

    // Cargar los profesores al iniciar la página
    cargarProfesoresConMaterias();
});
