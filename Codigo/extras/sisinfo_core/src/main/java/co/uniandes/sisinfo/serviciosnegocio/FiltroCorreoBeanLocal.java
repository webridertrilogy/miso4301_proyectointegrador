/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

import co.uniandes.sisinfo.entities.FiltroCorreo;

/**
 *
 * @author Asistente
 */
@Local
public interface FiltroCorreoBeanLocal {

    FiltroCorreo evaluarFiltros(String para, String asunto, String cc, String cco, String archivo, String mensaje);
    
}
