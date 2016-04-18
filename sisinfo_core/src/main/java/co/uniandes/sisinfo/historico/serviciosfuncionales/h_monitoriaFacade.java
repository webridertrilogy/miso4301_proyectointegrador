/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.historico.entities.h_monitoria;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author david
 */
@Stateless
public class h_monitoriaFacade implements h_monitoriaFacadeLocal, h_monitoriaFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(h_monitoria h_monitoria) {
        em.persist(h_monitoria);
    }

    public void edit(h_monitoria h_monitoria) {
        em.merge(h_monitoria);
    }

    public void remove(h_monitoria h_monitoria) {
        em.remove(em.merge(h_monitoria));
    }

    public h_monitoria find(Object id) {
        return em.find(h_monitoria.class, id);
    }

    public List<h_monitoria> findAll() {
        return em.createQuery("select object(o) from h_monitoria as o").getResultList();
    }

    public List<h_monitoria> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_monitoria as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_monitoria as o").getSingleResult()).intValue();
    }

}
