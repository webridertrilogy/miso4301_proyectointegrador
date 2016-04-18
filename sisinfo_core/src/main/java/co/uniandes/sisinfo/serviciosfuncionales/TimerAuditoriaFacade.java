/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TimerAuditoria;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Manuel
 */
@Stateless
public class TimerAuditoriaFacade implements TimerAuditoriaFacadeLocal,TimerAuditoriaFacadeRemote{

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(TimerAuditoria timerAudit) {
        em.persist(timerAudit);
    }

    @Override
    public void edit(TimerAuditoria timerAudit) {
        em.merge(timerAudit);
    }

    @Override
    public void remove(TimerAuditoria timerAudit) {
        em.remove(em.merge(timerAudit));
    }

    @Override
    public TimerAuditoria find(Object id) {
        return em.find(TimerAuditoria.class, id);
    }

    @Override
    public List<TimerAuditoria> findAll() {
        return em.createQuery("select object(o) from TimerAuditoria as o").getResultList();
    }
}
