document.addEventListener("DOMContentLoaded", () => {
    const profesorSelect = document.getElementById("profesorSelect");
    const cursoSelect = document.getElementById("cursoSelect");
    const materiaSelect = document.getElementById("materiaSelect");
    const asignarMateriaForm = document.getElementById("asignarMateriaForm");
    const profesorEliminarSelect = document.getElementById("profesorEliminarSelect");
    const eliminarProfesorForm = document.getElementById("eliminarProfesorForm");

    // Cargar los profesores
    fetch("http://localhost:8080/api/profesores")
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(profesores => {
            profesores.forEach(profesor => {
                const option = document.createElement("option");
                option.value = profesor.id;
                option.textContent = profesor.nombreCompleto;
                profesorSelect.appendChild(option);
            });
        })
        .catch(error => console.error("Error al cargar profesores:", error));

    // Cargar los cursos
    fetch("http://localhost:8080/api/cursos")
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(cursos => {
            cursos.forEach(curso => {
                const option = document.createElement("option");
                option.value = curso.id;
                option.textContent = `Curso ${curso.nombreCurso}`;
                cursoSelect.appendChild(option);
            });
        })
        .catch(error => console.error("Error al cargar cursos:", error));

    // Cargar materias según el curso seleccionado
    cursoSelect.addEventListener("change", (event) => {
        const cursoId = event.target.value;

        // Resetear el dropdown de materias
        materiaSelect.innerHTML = '<option value="" disabled selected>Selecciona una materia</option>';
        materiaSelect.disabled = true;

        fetch(`http://localhost:8080/api/materias/curso/${cursoId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(materias => {
                materias.forEach(materia => {
                    const option = document.createElement("option");
                    option.value = materia.id;
                    option.textContent = materia.nombre;
                    materiaSelect.appendChild(option);
                });
                materiaSelect.disabled = false;
            })
            .catch(error => console.error("Error al cargar materias:", error));
    });

    // Manejar el envío del formulario para asignar la materia al profesor
    asignarMateriaForm.addEventListener("submit", (event) => {
        event.preventDefault();
    
        const profesorId = profesorSelect.value; // ID del profesor seleccionado
        const materiaId = materiaSelect.value;  // ID de la materia seleccionada
        const cursoId = cursoSelect.value;      // ID del curso seleccionado
    
        // Validar que todos los valores sean correctos
        if (!profesorId || !materiaId || !cursoId) {
            Swal.fire({
                icon: "error",
                title: "Error",
                text: "Por favor selecciona un profesor, curso y materia.",
                confirmButtonColor: "#d33",
                confirmButtonText: "Cerrar"
            });
            return;
        }
    
        // Enviar la solicitud POST al backend con todos los datos requeridos
        fetch(`http://localhost:8080/api/profesores/${profesorId}/materias`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ materiaId, cursoId }) // Enviar ambos valores
        })
            .then(response => {
                if (response.ok) {
                    return response.text().then(message => {
                        Swal.fire({
                            icon: "success",
                            title: "Éxito",
                            text: message,
                            confirmButtonColor: "#3085d6",
                            confirmButtonText: "Cerrar"
                        });
                    });
                } else if (response.status === 409) {
                    return response.text().then(message => {
                        Swal.fire({
                            icon: "error",
                            title: "Error",
                            text: message,
                            confirmButtonColor: "#d33",
                            confirmButtonText: "Cerrar"
                        });
                    });
                } else {
                    throw new Error("Error al asignar materia.");
                }
            })
            .catch(error => {
                console.error("Error al asignar materia:", error);
                Swal.fire({
                    icon: "error",
                    title: "Error",
                    text: "No se pudo asignar la materia. Por favor, inténtalo de nuevo.",
                    confirmButtonColor: "#d33",
                    confirmButtonText: "Cerrar"
                });
            });
    });

    // Función para cargar profesores (basado en sus correos)
    function cargarProfesoresParaEliminar() {
        fetch("http://localhost:8080/api/profesores")
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(profesores => {
                profesorEliminarSelect.innerHTML = ""; // Limpiar el dropdown
                profesores.forEach(profesor => {
                    const option = document.createElement("option");
                    option.value = profesor.id;
                    option.textContent = profesor.email; // Mostrar el correo
                    profesorEliminarSelect.appendChild(option);
                });
            })
            .catch(error => console.error("Error al cargar profesores:", error));
    }

    // Llamar a la función para cargar profesores
    cargarProfesoresParaEliminar();

    // Manejar la eliminación del profesor
    eliminarProfesorForm.addEventListener("submit", (event) => {
        event.preventDefault();

        const profesorId = profesorEliminarSelect.value;

        if (!profesorId) {
            Swal.fire({
                icon: "error",
                title: "Error",
                text: "Por favor selecciona un profesor para eliminar.",
                confirmButtonColor: "#d33",
                confirmButtonText: "Cerrar"
            });
            return;
        }

        fetch(`http://localhost:8080/api/profesores/${profesorId}`, {
            method: "DELETE"
        })
            .then(response => {
                if (response.ok) {
                    return response.text().then(message => {
                        Swal.fire({
                            icon: "success",
                            title: "Éxito",
                            text: message,
                            confirmButtonColor: "#3085d6",
                            confirmButtonText: "Cerrar"
                        });
                        // Recargar la lista de profesores
                        cargarProfesoresParaEliminar();
                    });
                } else {
                    throw new Error("Error al eliminar el profesor.");
                }
            })
            .catch(error => {
                console.error("Error al eliminar el profesor:", error);
                Swal.fire({
                    icon: "error",
                    title: "Error",
                    text: "No se pudo eliminar el profesor. Por favor, inténtalo de nuevo.",
                    confirmButtonColor: "#d33",
                    confirmButtonText: "Cerrar"
                });
            });
    });
});
