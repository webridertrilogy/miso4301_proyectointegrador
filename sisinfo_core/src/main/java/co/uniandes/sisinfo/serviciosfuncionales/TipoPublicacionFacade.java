/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TipoPublicacion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Asistente
 */
@Stateless
public class TipoPublicacionFacade implements TipoPublicacionFacadeLocal, TipoPublicacionFacadeRemote {
       @PersistenceContext
    private EntityManager em;

    public void create(TipoPublicacion tipoPublicacion) {
        em.persist(tipoPublicacion);
    }

    public void edit(TipoPublicacion tipoPublicacion) {
        em.merge(tipoPublicacion);
    }

    public void remove(TipoPublicacion tipoPublicacion) {
        em.remove(em.merge(tipoPublicacion));
    }

    public TipoPublicacion find(Object id) {
        return em.find(TipoPublicacion.class, id);
    }

    public List<TipoPublicacion> findAll() {
        return em.createQuery("select object(o) from TipoPublicacion as o").getResultList();
    }

    public List<TipoPublicacion> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from TipoPublicacion as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from TipoPublicacion as o").getSingleResult()).intValue();
    }

    public TipoPublicacion findByTipoPublicacion(String tipoPublicacion) {
        try {
            return (TipoPublicacion) em.createNamedQuery("TipoPublicacion.findByTipoPublicacion").setParameter("tipoPublicacion", tipoPublicacion).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
             return (TipoPublicacion) em.createNamedQuery("TipoPublicacion.findByTipoPublicacion").setParameter("tipoPublicacion", tipoPublicacion).getResultList().get(0);
        }
    }



}
