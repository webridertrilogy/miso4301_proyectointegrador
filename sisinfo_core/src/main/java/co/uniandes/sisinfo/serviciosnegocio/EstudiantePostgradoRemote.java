package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Servicios de administración de estudiantes de postgrado (Interface Remota)
 * @author David Naranjo, Camilo Cortés, Marcela Morales
 */
@Remote
public interface EstudiantePostgradoRemote {

    /**
     * Método que se encarga de actualizar la hoja de vida de un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String actualizarHojaVida(String xml);

    /**
     * Método que se encarga de actualizar la información académica de un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String actualizarInformacionAcademica(String xml);

    /**
     * Método que se encarga de actualizar la información personal de un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String actualizarInformacionPersonal(String xml);
    

    /**
     * Método que se encarga de consultar la información de un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarEstudiante(String xml);

    /**
     * Método que se encarga de consultar la información de un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarEstudiantePorId(String xml);

    /**
     * Método que se encarga de consultar todos los estudiantes dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarEstudiantes(String xml);

    /**
     * Método que se encarga de consultar la hoja de vida de un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarHojaVida(String xml);

    /**
     * Método que se encarga de consultar la información académica de un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarInformacionAcademica(String xml);

    /**
     * Método que se encarga de consultar la información personal de un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String consultarInformacionPersonal(String xml);

    /**
     * Método que se encarga de crear un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String crearEstudiante(String xml);

    /**
     * Método que se encarga de eliminar un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String eliminarEstudiante(String xml);

    /**
     * Reporte de hojas de vida en la Bolsa de Empleo
     * @param xml
     * @return la ruta en la que el reporte ha sido persistido
     */
    String hacerReporteTodasHojasDeVida(String xml);
}
