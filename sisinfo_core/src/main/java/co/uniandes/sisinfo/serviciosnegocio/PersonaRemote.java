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
public interface PersonaRemote {

    String crearPersona(String comando);

    String editarPersona(String comando);

    String darPersonaPorCorreo(String comando);
}
