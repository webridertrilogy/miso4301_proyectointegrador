/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface CorreoSinEnviarBeanLocal {

    void agregarCorreo(String para, String asunto, String cc, String cco, String archivo, String mensaje);
    
}