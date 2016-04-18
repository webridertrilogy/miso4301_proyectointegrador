/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.Dia;
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
public class DiaFacade implements DiaFacadeLocal, DiaFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(Dia dia) {
        em.persist(dia);
    }

    public void edit(Dia dia) {
        em.merge(dia);
    }

    public void remove(Dia dia) {
        em.remove(em.merge(dia));
    }

    public Dia find(Object id) {
        return em.find(Dia.class, id);
    }

    public List<Dia> findAll() {
        return em.createQuery("select object(o) from Dia as o").getResultList();
    }

    public List<Dia> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from Dia as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Dia as o").getSingleResult()).intValue();
    }

}
