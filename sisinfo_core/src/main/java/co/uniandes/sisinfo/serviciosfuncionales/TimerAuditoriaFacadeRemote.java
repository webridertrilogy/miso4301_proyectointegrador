/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TimerAuditoria;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Manuel
 */
@Remote
public interface TimerAuditoriaFacadeRemote {
    void create(TimerAuditoria timerAudit);

    void edit(TimerAuditoria timerAudit);

    void remove(TimerAuditoria timerAudit);

    TimerAuditoria find(Object id);

    List<TimerAuditoria> findAll();
}
