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

import javax.ejb.Local;

/**
 * Interface Local
 * Servicio de negocio: Preselección
 */
@Local
public interface PreseleccionLocal {

    /**
     * Método que se encarga de preseleccionar dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String preseleccionar(String comando);

    /**
     * Método que se encarga de revertir una preselección dado el comando respectivo
     * Retorna true en caso de que la acción sea realizada, false en caso contrario
     * @param xml Cadena XML en la cual se define el comando
     * @param responsable Responsable de revertir la preseleccion
     * @param motivo Motivo por el cual se revierte la preseleccion
     * @param mensajeEstudiante Mensaje que se le enviara al estudiante
     * @return true|false Resultado de revertir la preselección
     */
    boolean revertirPreseleccion(String idSolicitud,String responsable,String motivo,String mensajeEstudiante);

    /**
     * Método que se encarga de consultar las secciones sin preselección dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darSeccionesSinPreseleccion(String xml);

    String despreseleccionar(String String);

    String preseleccionarMonitoresT2(String comando);

    boolean revertirPreseleccionAutomaticamente(String idSolicitud);
}
