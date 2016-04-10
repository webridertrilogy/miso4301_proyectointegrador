/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author da-naran
 */
@Remote
public interface GrupoRemote {

    String crearGrupo(String comando);
   
    String removerPersonasDeGrupo(String comando);

    String eliminarGrupo(String comando);

    String darGrupos(String comando);

    String darGrupoPorID(String comando);

    String darGrupoPorNombre(String comando);

    String darGruposPorDuenho(String xml);

    String editarGrupoPersonas(String xml);

    String programarBorradoGrupos(String xml);

    void manejoTimmerGrupoBean(String comando);
}
