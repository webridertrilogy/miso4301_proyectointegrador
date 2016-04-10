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
public interface AdministradorSisinfoBeanRemote {

    void enviarCorreoDiagnosticoSisinfo(String x);
    
}
