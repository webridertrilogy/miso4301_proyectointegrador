/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.util.Collection;

import javax.ejb.Remote;

import co.uniandes.sisinfo.bo.AccionBO;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Remote
public interface ProyectoDeGradoBeanRemote {

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

    void manejoTimmersTesisPregrado(String comando);

    String consultarCateoriasProyectosDeGrado(String xml);

    String darPeriodosConfigurados(String xml);

    String darTodosLosTemasDeProyectoDeGrado(String xml);

    String enviarPropuestaProyectoGrado(String xml);

    String enviarAficheProyectoGrado(String comandoXML);

    String enviarAprobacionAficheProyectoGrado(String comandoXML);

    String aceptarPropuestaTesisPorAsesor(String xml);

    String crearTareaEnviarPropuestaPY();

    String darProyectoDeGradoPorId(String xml);

    String darTemaProyectoGradoPorId(String xml);

    String agregarComentarioProyectoGrado(String xml);

    String darComentariosProyectoGrado(String xml);

    String subirDocumentoABETProyectoGrado(String comandoXML);

    String subirNotaProyectoGrado(String comandoXML);

    String enviarInformeRetiroProyectoGrado(String comandoXML);

    String enviarInformePendienteProyectoGrado(String comandoXML);

    public String enviarInformePendienteEspecialProyectoGrado(String comandoXML);

    String enviarAprobacionPendienteProyectoGrado(String comandoXML);

    String enviarAprobacionPendienteEspecialProyectoGrado(String comandoXML);

    public abstract void migrarTesisRetiradas();

    public abstract void migrarTesisPerdidas();

    void migrarTesisTerminadas();

    public String darProyectosPregradoAMigrar(String xml);

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

    String comportamientoEmergenciaMigrarProyectoDeGrado(String xml);

    Collection<AccionBO> darAcciones(String rol, String login);

    String darFechasProyectoGradoPeriodoActual(String xml);
}
