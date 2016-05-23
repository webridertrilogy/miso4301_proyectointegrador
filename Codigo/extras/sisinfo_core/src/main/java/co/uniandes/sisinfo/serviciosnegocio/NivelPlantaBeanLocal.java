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
public interface NivelPlantaBeanLocal {

    String consultarNivelesPlanta(String comando);
    
}
