/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 * @author Juan Manuel Moreno B.
 */
@Local
public interface AuditoriaBeanLocal {

    /**
     * Guarda un comando
     * @param xml
     * @return
     */
    void guardarXML(String nombre, String xml);

    /**
     * Consulta lista blanca.
     */
    String getListaBlanca(String xml);

    /**
     * Elimina un error de lista blanca.
     * @param xml
     * @return
     */
    String eliminarErrorListaBlanca(String xml);

    /**
      * Consulta lista de comandos para guardar.
      */
    String getListaComandoXML(String xml);

    /**
     * Elimina un comando de lista de comandos.
     * @param xml
     * @return
     */
    String eliminarComandoListaComando(String xml);
}
