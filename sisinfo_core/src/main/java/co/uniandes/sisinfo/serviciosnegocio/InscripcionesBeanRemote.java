/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 *
 * @author im.melo33
 */
@Remote
public interface InscripcionesBeanRemote {

    String crearInscripcion(String xml);

    String editarInscripcion(String xml);

    String cerrarInscripcion(String xml);

    String eliminarInscripcion(String xml);

    String darDetallesAdmonInscripcion(String xml);

    String darListaInscripcionesAdmin(String xml);

    String darInscripciones(String xml);

    //-----------metodos de los usuarios----


    String darListaInscripcionesUsuarioPorCorreo(String xml);

    String modificarInscripcionUsuario(String xml);

    String darInscripcionUsuarioPorId(String xml);

    @Deprecated
    void manejarTimerCierreInscripcion(String info);

    void manejoTimersInscripcion(String info);

    String modificarInscripcionUsuarioPorCorreo(String xml);
    
}
