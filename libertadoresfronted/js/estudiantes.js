document.addEventListener("DOMContentLoaded", () => {
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
});
