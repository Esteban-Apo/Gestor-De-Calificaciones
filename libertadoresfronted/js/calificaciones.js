document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const cursoId = urlParams.get("cursoId");
    const materiaId = urlParams.get("materiaId");
    const listaEstudiantes = document.getElementById("ListaestudiantesCalificaciones");
    const botonGuardar = document.getElementById("guardar-calificaciones");

    // Cargar la lista de estudiantes para este curso y materia
    function cargarEstudiantes() {
        fetch(`http://localhost:8080/api/estudiantes/curso/${cursoId}/materia/${materiaId}`)
            .then(response => response.json())
            .then(estudiantes => {
                listaEstudiantes.innerHTML = ""; // Limpiar lista previa
    
                estudiantes.forEach(estudiante => {
                    const row = document.createElement("tr");
                    row.setAttribute("data-estudiante-id", estudiante.id);  // Aquí agregamos el id al atributo data
    
    
                    // Nombre y correo del estudiante
                    const nombreCell = document.createElement("td");
                    nombreCell.textContent = estudiante.nombre;
                    const correoCell = document.createElement("td");
                    correoCell.textContent = estudiante.email;
    
                    // Campos de calificación por periodo
                    const periodo1Cell = document.createElement("td");
                    const periodo1Input = document.createElement("input");
                    periodo1Input.type = "number";
    
                    const periodo2Cell = document.createElement("td");
                    const periodo2Input = document.createElement("input");
                    periodo2Input.type = "number";
    
                    const periodo3Cell = document.createElement("td");
                    const periodo3Input = document.createElement("input");
                    periodo3Input.type = "number";
    
                    // Consultar calificaciones previas
                    fetch(`http://localhost:8080/api/calificaciones/estudiante/${estudiante.id}/materia/${materiaId}`)
                        .then(res => res.json())
                        .then(calificacion => {
                            // Si hay calificaciones previas, se llenan los campos
                            periodo1Input.value = calificacion.periodo1 ?? "";
                            periodo2Input.value = calificacion.periodo2 ?? "";
                            periodo3Input.value = calificacion.periodo3 ?? "";
                        })
                        .catch(() => {
                            // Si no hay calificaciones, los campos se quedan vacíos
                            periodo1Input.value = "";
                            periodo2Input.value = "";
                            periodo3Input.value = "";
                        });
    
                    periodo1Cell.appendChild(periodo1Input);
                    periodo2Cell.appendChild(periodo2Input);
                    periodo3Cell.appendChild(periodo3Input);
    
                    // Añadir todas las celdas a la fila
                    row.appendChild(nombreCell);
                    row.appendChild(correoCell);
                    row.appendChild(periodo1Cell);
                    row.appendChild(periodo2Cell);
                    row.appendChild(periodo3Cell);
    
                    // Botón de guardar calificación
                    const accionesCell = document.createElement("td");
                    const guardarButton = document.createElement("button");
                    guardarButton.textContent = "Guardar";
                    guardarButton.onclick = () => guardarCalificaciones(estudiante.id, periodo1Input.value, periodo2Input.value, periodo3Input.value);
                    accionesCell.appendChild(guardarButton);
                    row.appendChild(accionesCell);
    
                    listaEstudiantes.appendChild(row);
                });
            })
            .catch(error => console.error("Error al cargar estudiantes:", error));
    }
     // Guardar todas las calificaciones de una vez
    botonGuardar.addEventListener("click", function () {
        const calificaciones = [];
    
        // Obtener las calificaciones de todos los estudiantes en la lista
        const rows = document.querySelectorAll("#ListaestudiantesCalificaciones tr");
        rows.forEach(row => {
            const inputs = row.querySelectorAll("input");
            const estudianteId = row.getAttribute("data-estudiante-id");  // Aquí obtenemos el id del estudiante
            if (inputs.length === 3) {
                const periodo1 = parseFloat(inputs[0].value);
                const periodo2 = parseFloat(inputs[1].value);
                const periodo3 = parseFloat(inputs[2].value);
    
                calificaciones.push({
                    estudianteId: estudianteId,  // Ahora se pasa el id correcto
                    periodo1: periodo1,
                    periodo2: periodo2,
                    periodo3: periodo3,
                    materiaId: materiaId
                });
            }
        });
    
        fetch("http://localhost:8080/api/calificaciones", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(calificaciones)  // Enviamos el array de calificaciones
        })
        .then(response => {
            if (response.ok) {  // Verifica si la respuesta es exitosa (200-299)
                Swal.fire({
                    icon: "success",
                    title: "Calificaciones guardadas",
                    text: "Las calificaciones se han guardado exitosamente.",
                    confirmButtonText: "Aceptar"
                });
            } else {
                throw new Error("Error en la respuesta del servidor");  // Si no es ok, lanzar un error
            }
        })
        .catch(error => {
            console.error("Error al guardar calificaciones:", error);  // Imprimir error en consola para más detalles
            Swal.fire({
                icon: "error",
                title: "Error al guardar calificaciones",
                text: "Hubo un problema al guardar las calificaciones. Intenta nuevamente.",
                confirmButtonText: "Entendido"
            });
        });
        
    });

    // Cargar estudiantes al iniciar
    cargarEstudiantes();
});

