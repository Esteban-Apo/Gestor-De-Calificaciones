const verificarCurso = async (estudianteId) => {
    try {
        const response = await fetch(`http://localhost:8080/api/estudiantes/${estudianteId}`);
        if (response.ok) {
            const estudiante = await response.json();
            return estudiante.curso ? estudiante.curso.nombre : null; // Devuelve el nombre del curso si está asignado
        }
        throw new Error("Error al verificar el curso del estudiante");
    } catch (error) {
        Swal.fire({
            title: 'Error',
            text: error.message,
            icon: 'error',
            confirmButtonText: 'Entendido',
        });
        throw error; // Propaga el error para que se pueda manejar en otros métodos
    }
};

// Escuchar el evento submit del formulario
document.getElementById('formAgregarEstudianteCurso').addEventListener('submit', async function (e) {
    e.preventDefault();

    const email = document.getElementById('emailEstudiante').value;
    const cursoId = document.getElementById('cursoSelect').value;

    try {
        // Obtener ID del estudiante por su email (petición al backend)
        const estudianteResponse = await fetch(`http://localhost:8080/api/estudiantes?email=${email}`);
        if (!estudianteResponse.ok) {
            throw new Error("Estudiante no encontrado");
        }
        const estudiante = await estudianteResponse.json();

        // Verificar si el estudiante ya está asignado a un curso
        const cursoActual = await verificarCurso(estudiante.id);
        if (cursoActual) {
            throw new Error(`El estudiante ya está asignado al curso: ${cursoActual}`);
        }

        // Asociar estudiante al curso
        const asignacionResponse = await fetch(`http://localhost:8080/api/cursos/${cursoId}/estudiantes/${estudiante.id}`, {
            method: 'POST',
        });

        if (!asignacionResponse.ok) {
            // Si hay un error, obtenemos el mensaje del backend
            const errorMessage = await asignacionResponse.text();
            throw new Error(errorMessage);
        }

        // Éxito
        Swal.fire({
            title: 'Éxito',
            text: 'Estudiante agregado exitosamente',
            icon: 'success',
            timer: 2000,
            showConfirmButton: false,
        });
    } catch (error) {
        // Manejo de errores con SweetAlert2
        Swal.fire({
            title: 'Error',
            text: error.message,
            icon: 'error',
            confirmButtonText: 'Entendido',
        });
    }
});
