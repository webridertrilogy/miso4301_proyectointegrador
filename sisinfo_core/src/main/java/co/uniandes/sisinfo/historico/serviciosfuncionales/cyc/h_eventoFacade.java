/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales.cyc;

import co.uniandes.sisinfo.historico.entities.cyc.h_evento;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Asistente
 */
@Stateless
public class h_eventoFacade implements h_eventoFacadeLocal, h_eventoFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(h_evento h_evento) {
        em.persist(h_evento);
    }

    public void edit(h_evento h_evento) {
        em.merge(h_evento);
    }

    public void remove(h_evento h_evento) {
        em.remove(em.merge(h_evento));
    }

    public h_evento find(Object id) {
        return em.find(h_evento.class, id);
    }

    public List<h_evento> findAll() {
        return em.createQuery("select object(o) from h_evento as o").getResultList();
    }

    public List<h_evento> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_evento as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_evento as o").getSingleResult()).intValue();
    }

}
