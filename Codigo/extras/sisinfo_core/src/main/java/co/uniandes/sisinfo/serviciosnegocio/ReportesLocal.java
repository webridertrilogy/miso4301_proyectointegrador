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
public interface ReportesLocal {
    
    /**
     * Método que se encarga de confirmar la subida de un archivo dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String hacerReporte(String comandoXML);


}
