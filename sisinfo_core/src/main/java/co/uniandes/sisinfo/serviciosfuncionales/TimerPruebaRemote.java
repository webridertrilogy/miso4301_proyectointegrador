/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import javax.ejb.Remote;

/**
 *
 * @author cf.morales46
 */
@Remote
public interface TimerPruebaRemote {

    void timer();
    
}
