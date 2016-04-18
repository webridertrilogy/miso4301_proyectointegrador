/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.Local;

import co.uniandes.sisinfo.entities.RangoFechas;
import co.uniandes.sisinfo.entities.Solicitud;
import co.uniandes.sisinfo.entities.datosmaestros.Seccion;

/**
 *
 * @author Administrador
 */
@Local
public interface RangoFechasBeanLocal {

    public boolean esRangoValido(String nombre);

    public RangoFechas consultarRangoFechaPorNombre(String nombre);

    void eliminarRangosFechas();

    String crearRangosFechas(String comando);

    void crearTareasConfirmacionMonitoria();

    void crearTareasTraerPapelesRegistrarConvenio();

    String editarRangosFechas(String comando);

    Timestamp darFechaInicialRangoPorNombre(String nombre);

    Timestamp darFechaFinalRangoPorNombre(String nombre);

    String consultarRangos(String comando);

    boolean rangosCreados();

    public String esRangoValidoXML(String xml);

    void abrirConvocatoria();

    /* METODOS VERSION 2.0
     */


      /**
     * metodo que crea una tarea de preseleccionar monitor para una seccion en un rango de fechas
     * @param seccion: seccion a la que se le debe crear la tarea
     */
     void crearTareaProfesorPreseleccionarPorSeccion(Seccion seccion);


 /**
  * metodo que le crea a un monitor la tarea de copnfirmar la preseleccion por parte de un profesor
  * @param idSolicitud: 
  */ void crearTareaMonitorPreseleccionadoConfirmarSeleccion(Long idSolicitud);

    /**
     * metodo encargado de crear la tarea de confirmar datos de un estudiante para coordinacion
     * @param idSol = id de la solicitud a la cual se le van a revisar los datos
     */
     void crearTareaVerificarDatosEstudiantePorParteCoordinacion(Long idSol);


    /**
     * Metodo que crea la tarea para un estudiante de traer papeles al departamento.
     * @param idSolicitud: id de la solicitud.
     * @param asunto Asunto del correo
     * @param mensaje Mensaje del correo
     */
     void crearTareaTraerPapelesRegistrarConvenio(Long id);


    /**
     * Metodo que crea una tarea para registrar firma convenio estudiantes
     * @param idSolicitud
     */ void crearTareaRegistarFirmaConveniosEstudiantes(Long idSolicitud);

      /**
     * MEtodo que crea un timer para revertir la seleccion de un estudiante por que no confirmo la monitoria en 3 dias.
     * @param fechaCaducacion: fecha cuando se vence el timer
     * @param solicitud: solicitud a revertir.
     * @param seccion: seccion a devolver estado.
     */
    void crearTimerRevertirSeleccionMonitorPorNoConfirmacionEn3Dias(Date fechaCaducacion, Solicitud solicitud);


    /**
     * MEtodo que crea un timer para revertir la seleccion de un estudiante por que no confirmo la monitoria en 3 dias.
     * @param fechaCaducacion: fecha cuando se vence el timer
     * @param solicitud: solicitud a revertir.
     * @param seccion: seccion a devolver estado.
     */
    //void crearTimerRevertirSeleccionMonitorPorNoConfirmacionEn3DiasMonitorT2APO(Date fechaCaducacion, Solicitud solicitud, Seccion seccion1, Seccion seccion2);

    void manejoTimersMonitorias(String info);

    void eliminarTimerConfirmacionEstudiante(Long idSolicitud);

    void realizarTareaVerificacionDatosEstudianteCoordinacion(Long id);
    
    /**
     * Realiza una tarea dado el correo del responsable, el tipo y la solicitud
     * a la que se encuentra asociada. El metodo solo realiza tareas que se
     * encuentren en estado pendiente
     * @param tipo Tipo de la tarea
     * @param idSol Id de la solicitud
     */
    void realizarTareaSolicitud(String tipo, Long idSol);

    /**
     * Metodo que informa si para una seccion la seleccion de medio monitor o menos esta a cargo de cupi2 o no
     * @param seccion: seccion que se desea conocer si la seleccion del medio monitor esta o no cargo de cupi2
     * @return TRUE si la seleccion esta a cargo de Cupi2: False si no esta a cargo de cupi2
     */
    boolean seleccionMedioMonitorNoACargoCupi2(Seccion seccion) ;

    String construirInformacionTimerRevertirSeleccion(Solicitud solicitud);

    void completarTareasPendientes(String responsable, String tipo, String idSolicitud);

    void realizarTareaSeccion(String tipo, String crn);

    void realizarTareaSeccionSolicitud(String tipo, String crn,Long idSol);

    void generarTareasNotaMonitor();

}
