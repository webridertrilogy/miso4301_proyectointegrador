/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Local
public interface ProyectoDeGradoBeanLocal {

    String crearSolicitudTesisProyectoGrado(String xml);

    String aceptarSolicitudProyectoGradoAsesor(String xml);

    String establecerFechasPeriodoPregrado(String xml);

    String crearTemaProyectoGrado(String xml);

    String eliminarTemaProyectoGrado(String xml);

    String darTemasProyectoGradoPorAsesor(String xml);

    String darProyectosDeGradoPorAsesor(String xml);

    String darProyectoDegradoEstudainte(String xml);

    String darProyectosDegradoPorEstado(String xml);

    String darProyectosDeGradoCoordinador(String xml);

    String darConfiguracionPeriodo(String xml);

    String darPeriodosConfigurados(String xml);

    String consultarCateoriasProyectosDeGrado(String xml);

    String darTodosLosTemasDeProyectoDeGrado(String xml);
   
    String enviarPropuestaProyectoGrado(String xml);

    String aceptarPropuestaTesisPorAsesor(String xml);

    String enviarAprobacionAficheProyectoGrado(String xml);

    String crearTareaEnviarPropuestaPY();

    String darProyectoDeGradoPorId(String xml);

    String darTemaProyectoGradoPorId(String xml);

    String agregarComentarioProyectoGrado(String xml);

    String darComentariosProyectoGrado(String xml);

    String subirDocumentoABETProyectoGrado(String comandoXML);

    String subirNotaProyectoGrado(String comandoXML);

    void manejoTimmersTesisPregrado(String comando);
    
    String enviarInformeRetiroProyectoGrado(String comandoXML);

    String enviarAficheProyectoGrado(String comandoXML);

    String enviarInformePendienteProyectoGrado(String comandoXML);

    String enviarInformePendienteEspecialProyectoGrado(String comandoXML);

    String enviarAprobacionPendienteProyectoGrado(String comandoXML);

    String enviarAprobacionPendienteEspecialProyectoGrado(String comandoXML);

    String consultarProyectosDeGradoParaExternos(String comandoXML);
    
    String confirmarInscripcionBannerProyectoGrado(String comandoXML);

    /**
     * Actualiza el estado del proyecto.
     */
    String actualizarProyectoGradoEstado(String xml);

    /**
     * Reprueba el proyecto de grado, asignandole la nota especificada dentro del comando
     * @param xml
     * @return
     */
    String reprobarProyectoGrado(String xml);

    String aprobarProyectoGradoCoordinacion(String xml);

    /**
     * Crea una solicitud de inscripci�n a Tesis Pregrado
     */
    String crearSolicitudProyectoXCoordinacion(String xml);
    
    void regenerarTareasCambioVersionSisinfo(String xml);

    String darProyectosPregradoAMigrar(String xml);

    String comportamientoEmergenciaMigrarProyectoDeGrado(String xml);

    String darFechasProyectoGradoPeriodoActual(String xml);
}
