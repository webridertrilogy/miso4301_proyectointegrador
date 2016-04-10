/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.RangoFechas;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class RangoFechasFacade implements RangoFechasFacadeLocal {

    @PersistenceContext(unitName = "EntitiesPU")
    private EntityManager em;

    public void create(RangoFechas rangoFechas) {
        em.persist(rangoFechas);
    }

    public void edit(RangoFechas rangoFechas) {
        em.merge(rangoFechas);
    }

    public void remove(RangoFechas rangoFechas) {
        em.remove(em.merge(rangoFechas));
    }

    public RangoFechas find(Object id) {
        return em.find(RangoFechas.class, id);
    }

    public List<RangoFechas> findAll() {
        return em.createQuery("select object(o) from RangoFechas as o").getResultList();
    }

    public List<RangoFechas> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from RangoFechas as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from RangoFechas as o").getSingleResult()).intValue();
    }

    public RangoFechas findByNombre(String nombre) {
        try {
            RangoFechas rango = (RangoFechas) em.createNamedQuery("RangoFechas.findByNombre").setParameter("nombre", nombre).getSingleResult();
            return rango;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            RangoFechas rango = (RangoFechas) em.createNamedQuery("RangoFechas.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
            return rango;
        }
    }
}
