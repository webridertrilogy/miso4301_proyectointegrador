/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ComentarioTesis;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Stateless
public class ComentarioTesisFacade implements ComentarioTesisFacadeLocal, ComentarioTesisFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(ComentarioTesis comentarioTesis) {
        em.persist(comentarioTesis);
    }

    public void edit(ComentarioTesis comentarioTesis) {
        em.merge(comentarioTesis);
    }

    public void remove(ComentarioTesis comentarioTesis) {
        em.remove(em.merge(comentarioTesis));
    }

    public ComentarioTesis find(Object id) {
        return em.find(ComentarioTesis.class, id);
    }

    public List<ComentarioTesis> findAll() {
        return em.createQuery("select object(o) from ComentarioTesis as o").getResultList();
    }

    public List<ComentarioTesis> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from ComentarioTesis as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from ComentarioTesis as o").getSingleResult()).intValue();
    }

}
