/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.EstadoTesis;
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
public class EstadoTesisFacade implements EstadoTesisFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(EstadoTesis estadoTesis) {
        em.persist(estadoTesis);
    }

    public void edit(EstadoTesis estadoTesis) {
        em.merge(estadoTesis);
    }

    public void remove(EstadoTesis estadoTesis) {
        em.remove(em.merge(estadoTesis));
    }

    public EstadoTesis find(Object id) {
        return em.find(EstadoTesis.class, id);
    }

    public List<EstadoTesis> findAll() {
        return em.createQuery("select object(o) from EstadoTesis as o").getResultList();
    }

    public List<EstadoTesis> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from EstadoTesis as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from EstadoTesis as o").getSingleResult()).intValue();
    }

}
