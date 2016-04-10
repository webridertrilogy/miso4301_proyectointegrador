/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;


/**
 *
 * @author Manuel
 */
@Remote
public interface PublicacionesAcademiaBeanRemote {

    String crearPublicacion(String xml);

    String darPublicacion(String xml);

    String darPublicacionesPorCorreo(String xml);

    String darPublicacionesPorCorreoAnio(String xml);

    String darAniosDePublicaciones(String xml);

    String darPublicadoresPublicaciones(String xml);

    String modificarPublicacion(String xml);

    void cargarPublicacionesAcademia();

    String actualizarPublicacionesAcademia(String xml);

}
