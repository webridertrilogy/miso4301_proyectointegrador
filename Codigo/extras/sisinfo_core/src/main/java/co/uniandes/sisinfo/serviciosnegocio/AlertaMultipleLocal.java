/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Carlos Morales
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

import co.uniandes.sisinfo.entities.AlertaMultiple;

/**
 * Interface Local
 * Servicio de negocio: Administración de alertas
 */
@Local
public interface AlertaMultipleLocal {

    /**
     * Crea una alerta automática con los datos vacios
     * @param tipo Tipo de la alerta
     * @param comando Comando de la alerta
     * @return
     */
    public AlertaMultiple crearAlertaAutomatica(String tipo,String comando);

    /**
     * Hace inactiva a una alerta dado su tipo.
     * @param comando Comando con el tipo de la alerta
     * @deprecated Usar pausarAlerta
     * @return
     */
    String borrarAlerta(String comando);

    /**
     * Retorna todas las alertas del sistema
     * @param root
     * @return
     */
    String consultarAlertas(String root);

    /**
     * Consulta las alertas dado un tipo
     * @param root
     * @return
     */
    String consultarAlertasTipoTarea(String root);

    /**
     * Crea las alertas utilizando el comando
     * @param comando
     * @return
     */
    String crearAlertas(String comando);

    /**
     * Edita una alerta ya existente
     * @param comando
     * @return
     */
    String editarAlertaGenerica(String comando);

    /**
     * Consulta las alertas y devuelve la información de las alertas sin incluir
     * las tareas respectivas
     * @param root
     * @return
     */
    String consultarAlertasSinTareas(String root);

    /**
     * Retorna todas las periodicidades disponibles
     * @param comando
     * @return
     */
    String darPeriodicidades(String comando);

    /**
     * Retorna todos los tipos de las tareas existentes
     * @param comando
     * @return
     */
    String darTiposAlertas(String comando);

    /**
     * Hace inactiva a una alerta dado su tipo.
     * @param comando Comando con el tipo de la alerta
     * @return
     */
    String pausarAlerta(String comando);

    /**
     * Hace activa a una alerta dado su tipo.
     * @param comando Comando con el tipo de la alerta
     * @return
     */
    String reanudarAlerta(String comando);

    /**
     * Metodo utilizado para la ejecucion periodica de la tarea.
     * @param tipo Tipo de la alerta a ejecutar
     */
    void ejecutarAlertaPorTipo(String tipo);

    


}
