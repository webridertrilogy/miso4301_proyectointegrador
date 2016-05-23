/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
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
public class SesionFacade implements SesionFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(Sesion sesion) {
        em.persist(sesion);
    }

    public void edit(Sesion sesion) {
        em.merge(sesion);
    }

    public void remove(Sesion sesion) {
        em.remove(em.merge(sesion));
    }

    public Sesion find(Object id) {
        return em.find(Sesion.class, id);
    }

    public List<Sesion> findAll() {
        return em.createQuery("select object(o) from Sesion as o").getResultList();
    }

    public List<Sesion> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from Sesion as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Sesion as o").getSingleResult()).intValue();
    }

    public void removeAll(){
        List<Sesion> sesiones = findAll();
        for (Sesion sesion : sesiones) {
            remove(sesion);
        }
    }
}
