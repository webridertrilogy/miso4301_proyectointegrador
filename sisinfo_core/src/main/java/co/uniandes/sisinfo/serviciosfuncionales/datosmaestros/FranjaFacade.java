/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.Franja;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author da-naran
 */
@Stateless
public class FranjaFacade implements FranjaFacadeLocal, FranjaFacadeRemote {
    @PersistenceContext(unitName = "EntitiesPU")
    private EntityManager em;

    public void create(Franja franja) {
        em.persist(franja);
    }

    public void edit(Franja franja) {
        em.merge(franja);
    }

    public void remove(Franja franja) {
        em.remove(em.merge(franja));
    }

    public Franja find(Object id) {
        return em.find(Franja.class, id);
    }

    public List<Franja> findAll() {
        return em.createQuery("select object(o) from Franja as o").getResultList();
    }

    public List<Franja> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from Franja as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Franja as o").getSingleResult()).intValue();
    }

}
