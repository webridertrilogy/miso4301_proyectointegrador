/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.despachador.services;

import javax.ejb.Local;

/**
 *
 * @author david
 */
@Local
public interface ColaProfesorLocal {

    public java.lang.String resolverComando(java.lang.String comandoXML);
    
}
