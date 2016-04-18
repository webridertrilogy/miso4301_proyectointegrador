/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Carlos Morales
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Properties;

import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicio de negocio: Administración de tareas
 */
@Remote
public interface TareaMultipleRemote {

     /**
     * Crea una nueva tarea multiple personal dada su información. Si la alerta
     * asociada a este tipo de tarea no existe, entonces la alerta es creada de
     * manera automática.
     * @param mensaje Mensaje de la tarea, usado para el correo y la descripcion
     * @param tipo Tipo de la tarea
     * @param correo Correo de la persona a la cual se asigna la tarea
     * @param agrupable Booleano que indica si la tarea es agrupable
     * @param header Header del mensaje utilizado en la tarea
     * @param footer Footer del mensaje utilizado en la tarea
     * @param fechaInicio Fecha inicial de la tarea
     * @param fechaFin Fecha final de la tarea
     * @param comando Comando de la tarea
     * @param nombreRol Rol de la persona a la que fue asignada la tarea
     * @param parametros Tabla con parametros de la tarea
     * @param asunto Asunto del correo asociado a esta tarea
     */
    void crearTareaPersona(String mensaje, String tipo, String correo, boolean agrupable, String header, String footer, Timestamp fechaInicio, Timestamp fechaFin, String comando, String nombreRol,HashMap<String,String> parametros,String asunto);

        /**
     * Crea una nueva tarea multiple personal dada su información. Si la alerta
     * asociada a este tipo de tarea no existe, entonces la alerta es creada de
     * manera automática.
     * @param mensajeCorreo Mensaje que se muestra en el correo de la tarea
     * @param mensajeDescripcion Mensaje que se muestra en la descripcion de la tarea
     * @param tipo Tipo de la tarea
     * @param correo Correo de la persona a la cual se asigna la tarea
     * @param agrupable Booleano que indica si la tarea es agrupable
     * @param header Header del mensaje utilizado en la tarea
     * @param footer Footer del mensaje utilizado en la tarea
     * @param fechaInicio Fecha inicial de la tarea
     * @param fechaFin Fecha final de la tarea
     * @param comando Comando de la tarea
     * @param nombreRol Rol de la persona a la que fue asignada la tarea
     * @param parametros Tabla con parametros de la tarea
     * @param asunto Asunto del correo asociado a esta tarea
     */
    void crearTareaPersona(String mensajeCorreo,String mensajeDescripcion, String tipo, String correo, boolean agrupable, String header, String footer, Timestamp fechaInicio, Timestamp fechaFin, String comando, String nombreRol,HashMap<String,String> parametros,String asunto);

    /**
     * Crea una nueva tarea multiple personal dada su información. Si la alerta
     * asociada a este tipo de tarea no existe, entonces la alerta es creada de
     * manera automática.
     * @param mensaje Mensaje de la tarea, usado para el correo y la descripcion
     * @param tipo Tipo de la tarea
     * @param correo Correo de la persona a la cual se asigna la tarea
     * @param agrupable Booleano que indica si la tarea es agrupable
     * @param header Header del mensaje utilizado en la tarea
     * @param footer Footer del mensaje utilizado en la tarea
     * @param fechaInicio Fecha inicial de la tarea
     * @param fechaFin Fecha final de la tarea
     * @param comando Comando de la tarea
     * @param nombreRol Rol de la persona a la que fue asignada la tarea
     * @param parametros Tabla con parametros de la tarea
     * @param asunto Asunto del correo asociado a esta tarea
     */
    void crearTareaRol(String mensaje, String tipo, String nombreRol, boolean agrupable, String header, String footer, Timestamp fechaInicio, Timestamp fechaFin, String comando, HashMap<String,String> parametros,String asunto);

        /**
     * Crea una nueva tarea multiple personal dada su información. Si la alerta
     * asociada a este tipo de tarea no existe, entonces la alerta es creada de
     * manera automática.
     * @param mensajeCorreo Mensaje que se muestra en el correo de la tarea
     * @param mensajeDescripcion Mensaje que se muestra en la descripcion de la tarea
     * @param tipo Tipo de la tarea
     * @param correo Correo de la persona a la cual se asigna la tarea
     * @param agrupable Booleano que indica si la tarea es agrupable
     * @param header Header del mensaje utilizado en la tarea
     * @param footer Footer del mensaje utilizado en la tarea
     * @param fechaInicio Fecha inicial de la tarea
     * @param fechaFin Fecha final de la tarea
     * @param comando Comando de la tarea
     * @param nombreRol Rol de la persona a la que fue asignada la tarea
     * @param parametros Tabla con parametros de la tarea
     * @param asunto Asunto del correo asociado a esta tarea
     */
    void crearTareaRol(String mensajeCorreo,String mensajeDescripcion, String tipo, String nombreRol, boolean agrupable, String header, String footer, Timestamp fechaInicio, Timestamp fechaFin, String comando, HashMap<String,String> parametros,String asunto);

    /**
     * Retorna las tareas dado un comando con el correo y un estado buscado
     * @param root Comando
     * @return
     */
    String darTareasCorreoEstado(String root);

    /**
     * Retorna las tareas dado un comando con el correo,un estado y un tipo
     * buscado
     * @param root Comando
     * @return
     */
    String darTareasCorreoEstadoTipo(String root);

    /**
     * Retorna el id de la tarea dado su tipo y el valor de los parametros de la
     * misma
     * @param root Comando
     * @return
     */
    String consultarIdTareaPorParametrosTipo(String xmlComando);

    /**
     * Consulta una tarea dado su id
     * @param root
     * @return
     */
    String consultarTareaId(String root);

    /**
     * Retorna las tareas vigentes dado un comando con el correo y un estado
     * @param root Comando
     * @return
     */
    String darTareasCorreoEstadoSinCaducar(String xmlComando);

    /**
     * Retorna los parametros utilizados por una tarea dado su tipo
     * @param xmlComando
     * @return
     * @deprecated
     */
    @Deprecated
    String darParametrosTipoTarea(String xmlComando);

    /**
     * Retorna el mensaje de correo que seria enviado por una tarea personal
     * durante su ejecucion.
     * @param tipo Tipo de la tarea
     * @param correo Correo del responsable
     * @return
     */
    String darMensajeTareaPersonalPorTipoYCorreo(String tipo,String correo);

    /**
     * Retorna el mensaje de correo que seria enviado por una tarea no personal
     * durante su ejecucion.
     * @param tipo Tipo de la tarea
     * @param correo Rol del responsable
     * @return
     */
    String darMensajeTareaNoPersonalPorTipoYRol(String tipo,String rol);

    /**
     * Retorna el mensaje que seria enviado por una tarea dado su tipo y
     * parametros
     * @param tipo
     * @param parametros
     * @return
     */
    String darCorreoTareaPorTipoYParametros(String tipo, HashMap<String,String> parametros);

    /**
     * Retorna el id de una tarea dado su tipo y parametros
     * @param tipo
     * @param params
     * @return
     */
    long consultarIdTareaPorParametrosTipo(String tipo, Properties params);

    void realizarTareaPorCorreo(String tipo, String correo,HashMap<String,String> params);

    void realizarTareaPorRol(String tipo, String rol, java.util.HashMap<String,String> params);

    public String darTareaCorreo(String comandoXML);

    String darTareasVencidasUsuario(String xml);

    void manejoTimerTareasVencidas(String timer);

    
}

