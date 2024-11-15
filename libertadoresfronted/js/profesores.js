document.addEventListener("DOMContentLoaded", function () {
    const btnAbrirModal = document.getElementById("btnAbrirModal");
    const modalAgregarProfesor = document.getElementById("modalAgregarProfesor");
    const btnCerrarModal = document.getElementById("btnCerrarModal");
    const formAgregarProfesor = document.getElementById("formAgregarProfesor");
    const profesoresList = document.getElementById("profesores-list");

     // Función para mostrar profesores en la lista
     function mostrarProfesor(profesor) {
        const profesorItem = document.createElement("li");
        profesorItem.textContent = `${profesor.nombreCompleto} - ${profesor.email} - ${profesor.celular}`;
        profesoresList.appendChild(profesorItem);
    }

    // Cargar y mostrar todos los profesores al cargar la página
    function cargarProfesores() {
        fetch("http://localhost:8080/api/profesores")
            .then(response => response.json())
            .then(profesores => {
                profesores.forEach(mostrarProfesor);
            })
            .catch(error => console.error("Error al cargar profesores:", error));
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
        
        const nombre = document.getElementById("nombre").value;
        const email = document.getElementById("email").value;
        const celular = document.getElementById("celular").value;

        // Crear objeto profesor
        const profesor = { nombreCompleto: nombre, email, celular };

        // Enviar datos al backend
        fetch("http://localhost:8080/api/profesores", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(profesor)
        })
        .then(response => {
            if (response.status === 409) {
                alert("El correo electrónico ya está en uso. Por favor, ingresa uno diferente.");
                throw new Error("Correo en uso");
            }
            return response.json();
        })
        .then(data => {
            // Agregar a la lista visualmente
            const profesorItem = document.createElement("li");
            profesorItem.textContent = `${data.nombreCompleto} - ${data.email} - ${data.celular}`;
            profesoresList.appendChild(profesorItem);

            // Limpiar el formulario y cerrar el modal
            formAgregarProfesor.reset();
            modalAgregarProfesor.style.display = "none";
        })
        .catch(error => console.error("Error al agregar profesor:", error));
    });

      // Cargar los profesores al iniciar la página
    cargarProfesores();
});
