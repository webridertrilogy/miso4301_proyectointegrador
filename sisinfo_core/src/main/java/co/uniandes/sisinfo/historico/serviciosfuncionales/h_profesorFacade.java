/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.historico.entities.h_profesor;
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
public class h_profesorFacade implements h_profesorFacadeLocal, h_profesorFacadeRemote {
    @PersistenceContext(unitName = "HistoricoPU")
    private EntityManager em;

    public void create(h_profesor h_profesor) {
        em.persist(h_profesor);
    }

    public void edit(h_profesor h_profesor) {
        em.merge(h_profesor);
    }

    public void remove(h_profesor h_profesor) {
        em.remove(em.merge(h_profesor));
    }

    public h_profesor find(Object id) {
        return em.find(h_profesor.class, id);
    }

    public List<h_profesor> findAll() {
        return em.createQuery("select object(o) from h_profesor as o").getResultList();
    }

    public List<h_profesor> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_profesor as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_profesor as o").getSingleResult()).intValue();
    }

}
