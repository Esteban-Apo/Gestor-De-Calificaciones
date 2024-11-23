document.getElementById('formAgregarEstudiante').addEventListener('submit', async function (e) {
    e.preventDefault();
    const email = document.getElementById('emailEstudiante').value;
    const cursoId = document.getElementById('cursoSelect').value;

    try {
        // Obtener el estudiante por correo
        const estudianteResponse = await fetch(`http://localhost:8080/api/estudiantes?email=${email}`);
        
        // Verificar si la respuesta es exitosa
        if (!estudianteResponse.ok) {
            const errorMessage = await estudianteResponse.text();
            alert(errorMessage);  // Muestra el mensaje de error desde el backend
            return;
        }
        
        // Obtener el estudiante
        const estudiante = await estudianteResponse.json();
        
        if (estudiante && estudiante.id) {
            // Asociar estudiante al curso
            await fetch(`http://localhost:8080/api/cursos/${cursoId}/estudiantes/${estudiante.id}`, {
                method: 'POST',
            });
            alert('Estudiante agregado exitosamente');
        } else {
            alert('Estudiante no encontrado');
        }
    } catch (error) {
        alert(error.message);
    }
});
