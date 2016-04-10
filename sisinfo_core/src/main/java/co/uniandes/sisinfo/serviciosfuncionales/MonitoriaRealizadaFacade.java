/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.MonitoriaRealizada;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author david
 */
@Stateless
public class MonitoriaRealizadaFacade implements MonitoriaRealizadaFacadeLocal, MonitoriaRealizadaFacadeRemote {
    @PersistenceContext(unitName = "EntitiesPU")
    private EntityManager em;

    public void create(MonitoriaRealizada monitoriaRealizada) {
        em.persist(monitoriaRealizada);
    }

    public void edit(MonitoriaRealizada monitoriaRealizada) {
        em.merge(monitoriaRealizada);
    }

    public void remove(MonitoriaRealizada monitoriaRealizada) {
        em.remove(em.merge(monitoriaRealizada));
    }

    public MonitoriaRealizada find(Object id) {
        return em.find(MonitoriaRealizada.class, id);
    }

    public List<MonitoriaRealizada> findAll() {
        return em.createQuery("select object(o) from MonitoriaRealizada as o").getResultList();
    }

    public List<MonitoriaRealizada> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from MonitoriaRealizada as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from MonitoriaRealizada as o").getSingleResult()).intValue();
    }
}
