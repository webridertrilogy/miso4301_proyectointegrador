/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ComentarioTesisPregrado;
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
public class ComentarioTesisPregradoFacade implements ComentarioTesisPregradoFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(ComentarioTesisPregrado comentarioTesisPregrado) {
        em.persist(comentarioTesisPregrado);
    }

    public void edit(ComentarioTesisPregrado comentarioTesisPregrado) {
        em.merge(comentarioTesisPregrado);
    }

    public void remove(ComentarioTesisPregrado comentarioTesisPregrado) {
        em.remove(em.merge(comentarioTesisPregrado));
    }

    public ComentarioTesisPregrado find(Object id) {
        return em.find(ComentarioTesisPregrado.class, id);
    }

    public List<ComentarioTesisPregrado> findAll() {
        return em.createQuery("select object(o) from ComentarioTesisPregrado as o").getResultList();
    }

    public List<ComentarioTesisPregrado> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from ComentarioTesisPregrado as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from ComentarioTesisPregrado as o").getSingleResult()).intValue();
    }

}
