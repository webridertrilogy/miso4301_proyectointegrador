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
public interface FiltroCorreoBeanRemote {

    String darFiltrosCorreo(String xml);

    String agregarFiltroCorreo(String xml);

    String eliminarFiltroCorreo(String xml);

    String editarFiltroCorreo(String xml);

    String darFiltroCorreo(String xml);

    String darTiposFiltroCorreo(String xml);

    String darOperacionesFiltroCorreo(String xml);
    
}
