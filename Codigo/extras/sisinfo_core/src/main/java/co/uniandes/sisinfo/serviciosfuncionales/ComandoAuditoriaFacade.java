/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ComandoAuditoria;
import co.uniandes.sisinfo.serviciosfuncionales.cache.SisinfoCache;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 *
* @author Paola Gómez
 */
@Stateless
//@Stateful
@EJB(name = "ComandoAuditoriaFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.ComandoAuditoriaFacadeLocal.class)
public class ComandoAuditoriaFacade implements ComandoAuditoriaFacadeLocal {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;
    private static SisinfoCache<ComandoAuditoria> cache;

    public static SisinfoCache<ComandoAuditoria> getCache() {
        if (cache == null) {
            cache = new SisinfoCache<ComandoAuditoria>();
            cache.setEternal(true);
        }
        return cache;
    }

    public void create(ComandoAuditoria comandoAuditoria) {
        em.persist(comandoAuditoria);
    }

    public void edit(ComandoAuditoria comandoAuditoria) {
        em.merge(comandoAuditoria);
    }

    public void remove(ComandoAuditoria comandoAuditoria) {
        em.remove(em.merge(comandoAuditoria));
    }

    public ComandoAuditoria find(Object id) {
        return em.find(ComandoAuditoria.class, id);
    }

    public List<ComandoAuditoria> findAll() {
        return em.createQuery("select object(o) from ComandoAuditoria as o").getResultList();
    }

    public ComandoAuditoria findById(Long id) {
        try {
            return (ComandoAuditoria) em.createNamedQuery("ComandoAuditoria.findById").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (ComandoAuditoria) em.createNamedQuery("ComandoAuditoria.findById").setParameter("id", id).getResultList().get(0);
        }
    }

    public ComandoAuditoria findByNombre(String nombre) {
        try {
            ComandoAuditoria comandoAuditoria = getCache().get(nombre);
            if (comandoAuditoria != null) {
                return comandoAuditoria;
            } else {
                Query query = em.createNamedQuery("ComandoAuditoria.findByNombre");
                ComandoAuditoria c = (ComandoAuditoria) query.setParameter("nombre", nombre).getSingleResult();
                getCache().put(nombre, c);
                return c;
            }
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (ComandoAuditoria) em.createNamedQuery("ComandoAuditoria.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
        }
    }
}
