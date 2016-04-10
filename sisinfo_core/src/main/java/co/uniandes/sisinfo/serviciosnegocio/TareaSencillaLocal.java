/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Carlos Morales
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.entities.TareaSencilla;
import java.util.HashMap;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicio de negocio: Administración de Tareas sencillas
 */
@Local
public interface TareaSencillaLocal {

    /**
     * Crea una nueva tarea sencilla dado el mensaje, una tabla con los
     * parametros y el tipo
     * @param mensaje Mensaje de la tarea
     * @param parametros Parametros de la tarea
     * @param tipo Tipo de la tarea
     * @return La tarea sencilla creada
     */
    TareaSencilla crearTareaSencilla(String mensaje,HashMap<String,String> parametros,String tipo);

    /**
     * Completa una tarea dado su id. El estado de la tarea sencilla es cambiado
     * a Terminada
     * @param id Id de la tarea sencilla a completar
     */
    void realizarTareaPorId(Long id);
}
