/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Remote
public interface InscripcionSubAreaInvestiBeanRemote
{
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

    String agregarCursoMaestria(String comando);

    String obtenerMateriaMaestriaPorId(String comando);
}
