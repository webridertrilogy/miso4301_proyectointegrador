/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import java.util.Date;
import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicios Timer Estudiante
 */
@Remote
public interface TimerEstudianteRemote {

    /**
     * Crea un timer dado el id de la solicitud, y el crn de la(s) sección(es)
     * @param idSolicitud Id de la solicitud
     * @param crn1 CRN de la sección 1
     * @param crn2 CRN de la sección 2
     * @return Fecha límite del timer
     */
    Date crearTimer(String idSolicitud, String crn1, String crn2);
}
