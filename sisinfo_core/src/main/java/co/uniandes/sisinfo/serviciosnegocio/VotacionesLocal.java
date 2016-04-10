/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 *
 * @author da-naran
 */
@Local
public interface VotacionesLocal {

    String crearVotacion(String comando);

    String votar(String comando);

    String darPersonasSinVotacion(String comando);

    String darVotacionesPorCorreo(String comando);

    String darVotacionesPorEstado(String comando);

    String darVotacionConCandidatos(String comando);

    String darVotaciones(String comando);

    String darResultadoVotacion(String comando);

    String darEstadoVotantesPorIdVotacion(String comando);

    String darVotacionConVotantes(String comando);

    void cerrarVotacion(String id);

    void manejoTimers(String parametro);
    
}
