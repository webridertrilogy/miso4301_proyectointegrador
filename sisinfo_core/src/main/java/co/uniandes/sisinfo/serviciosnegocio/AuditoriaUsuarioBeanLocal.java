/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;

import javax.ejb.Local;

/**
 *
 * @author Paola Gómez
 */
@Local
public interface AuditoriaUsuarioBeanLocal {

    void crearRegistroAuditoriaUsuario(String usuarioActual, String rol, String comando, String parametros, Timestamp fecha, Boolean accionExitosa);

    void crearRegistroAuditoriaUsuario(String xml, Boolean accionExitosa);

    String consultarAuditoriaUsuarioPorUsuarioFechaRolYComando(String xml);

    String consultarAuditoriaUsuarioActividad(String comandoXML);
}
