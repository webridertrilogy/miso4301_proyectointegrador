/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.historico.entities.h_monitor;
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
public class h_monitorFacade implements h_monitorFacadeLocal, h_monitorFacadeRemote {
    @PersistenceContext(unitName = "HistoricoPU")
    private EntityManager em;

    public void create(h_monitor h_monitor) {
        em.persist(h_monitor);
    }

    public void edit(h_monitor h_monitor) {
        em.merge(h_monitor);
    }

    public void remove(h_monitor h_monitor) {
        em.remove(em.merge(h_monitor));
    }

    public h_monitor find(Object id) {
        return em.find(h_monitor.class, id);
    }

    public List<h_monitor> findAll() {
        return em.createQuery("select object(o) from h_monitor as o").getResultList();
    }

    public List<h_monitor> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_monitor as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_monitor as o").getSingleResult()).intValue();
    }

    public h_monitor findByCorreo(String correo) {
        try{
            h_monitor result = (h_monitor)em.createNamedQuery("h_monitor.findByCorreo").setParameter("correo", correo).getSingleResult();
            return result;
        }
        catch(Exception e ){
            return null;
        }
        
    }


}
