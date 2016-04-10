/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import co.uniandes.sisinfo.entities.FiltroCorreo;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface FiltroCorreoBeanLocal {

    FiltroCorreo evaluarFiltros(String para, String asunto, String cc, String cco, String archivo, String mensaje);
    
}
