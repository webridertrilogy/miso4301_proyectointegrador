/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.despachador.services;

import javax.ejb.Local;

/**
 *
 * @author JuanCamilo
 */
@Local
public interface ColaConsultaLocal {

    String resolverComando(String comando);
    
}
