/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.NivelTesis;
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
public class NivelTesisFacade implements NivelTesisFacadeLocal, NivelTesisFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(NivelTesis nivelTesis) {
        em.persist(nivelTesis);
    }

    public void edit(NivelTesis nivelTesis) {
        em.merge(nivelTesis);
    }

    public void remove(NivelTesis nivelTesis) {
        em.remove(em.merge(nivelTesis));
    }

    public NivelTesis find(Object id) {
        return em.find(NivelTesis.class, id);
    }

    public List<NivelTesis> findAll() {
        return em.createQuery("select object(o) from NivelTesis as o").getResultList();
    }

    public List<NivelTesis> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from NivelTesis as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from NivelTesis as o").getSingleResult()).intValue();
    }

}
