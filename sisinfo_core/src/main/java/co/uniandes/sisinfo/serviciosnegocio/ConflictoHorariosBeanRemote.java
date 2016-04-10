/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface ConflictoHorariosBeanRemote {

    String crearPeticionConflictoHorario(String xml);

    String consultarPeticionesPorCorreo(String xml);

    String cancelarPeticionPorCorreo(String xml);

    String consultarCantidadPeticionesCursosISIS(String xml);

    String consultarPeticionesPorCodigoCursoYTipo(String xml);

    String actualizarEstadoYResolucionPeticiones(String xml);

    String consultarFechasConflictoHorario(String xml);

    String actualizarFechasConflictoHorario(String xml);

    String consultarProgramas(String xml);

    String consultarPeticionesResueltas(String xml);

    void eliminarPeticionesPorSeccion(Long id);
}
