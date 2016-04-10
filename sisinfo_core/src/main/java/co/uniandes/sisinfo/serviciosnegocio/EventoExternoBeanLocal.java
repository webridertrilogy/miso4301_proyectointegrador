/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface EventoExternoBeanLocal {

    String crearEditarEventoExterno(String xml);

    String consultarEventoExterno(String comandoXML);

    String consultarEventosExternos(String xml);

    String consultarCategorias(String xml);

    String editarCategorias(String xml);

    String consultarTiposCampoAdicional(String xml);

    String editarTiposCampo(String xml);

    String eliminarCategoria(String xml);

    String eliminarTipoCampo(String xml);

    String eliminarEventoExterno(String comandoXML);

    String cambiarEstadoEvento(String comandoXML);

    String subirImagenEventoExterno(String comandoXML);
}
