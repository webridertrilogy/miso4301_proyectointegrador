/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.entities.InscripcionSubareaInvestigacion;
import co.uniandes.sisinfo.entities.PeriodoTesis;

import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface InscripcionSubAreaInvestiBeanLocal {

    String obtenerMateriasMaestria(String xml);

    String obtenerMateriasMaestriaPorClasificacion(String xml);

    String darSlicitudIngresosubareaEstudiantePorCorreo(String xml);

    String aprobarInscripcionASubareaDirector(String xml);

    String aprobarInscripcionASubareaAsesor(String xml);

    String darSolicitudesIngresoSubareaParaCoordinador(String xml);

    String darSolicitudesIngresoSubAreaAsesor(String xml);

    String crearSolicitudIngresoSubareaEstudiante(String xml);

    String darSolicitudesAprobadasIngresoCoordinacion(String xml);

    String darSolicitudIngresoSubareaPorId(String xml);

    String crearSolicitudIngresoSubareaEstudiantePorCoordinacion(String xml);

    String darSolicitudesIngresoSubarea(String xml);

    /**
     * Metodos que antes eran privados de generar tareas y ahora son usados por TesisBean para volver a crear todas las tareas:
     */
     void crearTareaAsesorDeAprobarSubareaInscripcion(InscripcionSubareaInvestigacion inscripcion, PeriodoTesis periodoTesis, String mensajeBulletSinFormato) ;

     void crearTareaCoordinadorSubareaAprobarInscripcionSubarea(InscripcionSubareaInvestigacion inscrip);

    String agregarCursoMaestria(String comando);

    String obtenerMateriaMaestriaPorId(String comando);
}
