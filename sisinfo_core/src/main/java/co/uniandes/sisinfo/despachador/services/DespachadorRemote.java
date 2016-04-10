/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.despachador.services;

import javax.ejb.Remote;

/**
 *
 * @author ale-osor
 */
@Remote
public interface DespachadorRemote {
    String resolverComando(String comandoXML) throws Exception;
    
}
