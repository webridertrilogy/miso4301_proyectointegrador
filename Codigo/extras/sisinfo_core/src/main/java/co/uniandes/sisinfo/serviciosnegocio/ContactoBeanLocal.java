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
public interface ContactoBeanLocal {

    String consultarContactos(String comandoXml);

    String editarContacto(String xml);

    String consultarContacto(String comandoXml);

    String eliminarContacto(String xml);

    String agregarContacto(String xml);

    String enviarCorreo(String xml);

    String consultarSectoresCorporativos(String comandoXml);

    String borrarArchivo(String nombre);

    String darCargos(String nombre);

    String agregarInscripcion(String nombre);

    String loginPublico(String xml);

    String consultarInscritos(String xml);

    String registrarUsuarioPublico(String xml);

    String activarUsuarioPublico(String xml);

    String olvidoContrasena(String xml);

    String ConsultarUsuarioHashPublico(String xml);

    String cambiarContrasena(String xml);

    String consultarContactosFiltrados(String comandoXml) ;

    String consultarInscritrosEventoExterno(String comandoXml);
}
