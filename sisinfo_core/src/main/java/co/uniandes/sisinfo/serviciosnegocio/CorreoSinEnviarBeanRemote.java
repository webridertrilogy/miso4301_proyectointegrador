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
public interface CorreoSinEnviarBeanRemote {

    String darCorreosSinEnviar(String xml);

    String eliminarCorreosSinEnviar(String xml);

    String enviarCorreosSinEnviar(String xml);

    String darCorreoSinEnviar(String xml);

    String editarCorreoSinEnviar(String xml);
    
}
