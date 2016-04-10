/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author giovanni villegas
 */
@Remote
public interface EstudianteBeanRemote {

    String consultarEsEstudiantePorCorreo(String comando);

    String crearEstudiante(String comando);

    String consultarEstudiantes(String comando);

    String consultarEstudiante(String comando);

    String eliminarEstudiante(String comando);

    String actualizarEstadoActivoEstudiante(String comando);
}
