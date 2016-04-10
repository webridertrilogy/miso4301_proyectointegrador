/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Carlos Morales
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;


import java.util.Properties;
import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicio de negocio: Administración de tareas sencillas
 */
@Remote
public interface TareaSencillaRemote {

    /**
     * Completa una tarea dado su id. El estado de la tarea sencilla es cambiado
     * a Terminada
     * @param id Id de la tarea sencilla a completar
     */
    void realizarTareaPorId(Long id);

    /**
     * Completa todas las tareas dado el tipo de la tarea y sus parametros. El estado
     * de cada tarea que tenga el tipo y los parametros especificados se cambia a
     * Terminada
     * @param tipo Tipo de la tarea buscada
     * @param p Parametros de la tarea buscada
     */
    void realizarTareasPorTipoYParametros(String tipo, Properties p);
}

