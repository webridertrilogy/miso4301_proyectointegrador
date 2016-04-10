/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

//import co.uniandes.sisinfo.entities.Aspirante;
import java.util.Collection;
import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicio de negocio: Administración de Aspirantes
 */
@Remote
public interface AspiranteRemote {

    /**
     * Método que se encarga de consultar los datos personales dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darDatosPersonales(String xml);

    /**
     * Método que se encarga de consultar la información académica de un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darDatosAcademicos(String xml);

    /**
     * Método que se encarga de consultar la información de emergencia de un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darDatosEmergencia(String xml);


    /**
     * Método que se encarga de actualizar la información académica de un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String actualizarDatosAcademicos(String comando);

    /**
     * Método que se encarga de actualizar la información personal y de emergencia de un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String actualizarDatosPersonalesYEmergencia(String comando);

    String darMonitoriasOtrosDeptos(String comando);
}
