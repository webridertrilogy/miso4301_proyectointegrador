/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.IntencionPublicacion;
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
public class IntencionPublicacionFacade implements IntencionPublicacionFacadeLocal, IntencionPublicacionFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(IntencionPublicacion intencionPublicacion) {
        em.persist(intencionPublicacion);
    }

    public void edit(IntencionPublicacion intencionPublicacion) {
        em.merge(intencionPublicacion);
    }

    public void remove(IntencionPublicacion intencionPublicacion) {
        em.remove(em.merge(intencionPublicacion));
    }

    public IntencionPublicacion find(Object id) {
        return em.find(IntencionPublicacion.class, id);
    }

    public List<IntencionPublicacion> findAll() {
        return em.createQuery("select object(o) from IntencionPublicacion as o").getResultList();
    }

    public List<IntencionPublicacion> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from IntencionPublicacion as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from IntencionPublicacion as o").getSingleResult()).intValue();
    }




}
