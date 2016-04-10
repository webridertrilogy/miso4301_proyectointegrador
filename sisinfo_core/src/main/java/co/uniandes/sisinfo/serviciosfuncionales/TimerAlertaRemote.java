/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import java.util.Date;

import javax.ejb.Remote;
import javax.ejb.Timeout;
import javax.ejb.Timer;


/**
 *
 * @author cf.morales46
 */
@Remote
public interface TimerAlertaRemote {

     void crearTimer(long idAlerta, Date fechaAlerta,long intervalo);

    @Timeout
    public void ejecutarAlerta(Timer t);

    public void shutDownAlerta(long idAlerta);
    
}
