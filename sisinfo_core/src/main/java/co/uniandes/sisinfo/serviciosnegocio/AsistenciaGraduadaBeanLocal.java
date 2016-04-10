package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.util.Date;
import javax.ejb.Local;

/**
 * Servicios de administración de asistencias graduadas (interface local)
 * @author Marcela Morales
 */
@Local
public interface AsistenciaGraduadaBeanLocal {

    /**
     * Método que se encarga de crear una asistencia graduada dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String crearAsistenciaGraduada(String xml);

    /**
     * Método que se encarga de consultar el detalle de una asistencia graduada dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarAsistenciaGraduada(String xml);

    /**
     * Método que se encarga de consultar las asistencias graduadas dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarAsistenciasGraduadas(String xml);

    /**
     * Método que se encarga de consultar las asistencias graduadas de un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarAsistenciasGraduadasPorEstudiante(String xml);

    /**
     * Método que se encarga de consultar las asistencias graduadas de un profesor dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarAsistenciasGraduadasPorProfesor(String xml);

    /**
     * Método que se encarga de calificar una asistencia graduada dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String calificarAsistenciaGraduada(String xml);

    /**
     * Método que se encarga de consultar los tipos de asistencias graduadas dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarTiposAsistenciasGraduadas(String xml);

    /**
     * Método que se encarga de crear tareas para para que los profesores califiquen las asistencias graduadas del periodo actual
     */
    void crearTareaProfesorCalificarAsistenciaGraduada(Timestamp fechaInicio, Timestamp fechaFin);

    /**
     * Método que se encarga de crear tareas para para que los profesores califiquen las asistencias graduadas del periodo actual dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String crearTareaProfesorCalificarAsistenciaGraduada(String xml);

    /**
     * Método que se encarga de ejecutar el comportamiento luego de que los profesores califiquen las asistencias graduadas del periodo actual dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String comportamientoFinRangoCalificarAsistenciaGraduada(String xml);
}
