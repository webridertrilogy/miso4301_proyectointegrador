/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Constante;
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
 * @author JuanCamilo
 */
@Stateless
//@Stateful
@EJB(name = "ConstanteFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.ConstanteFacadeLocal.class)
public class ConstanteFacade implements ConstanteFacadeLocal {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;
    private static SisinfoCache<Constante> cache;

    public static SisinfoCache<Constante> getCache() {
        if (cache == null) {
            cache = new SisinfoCache<Constante>();
            cache.setEternal(true);
        }
        return cache;
    }

    public void create(Constante constante) {
        em.persist(constante);
    }

    public void edit(Constante constante) {
        em.merge(constante);
    }

    public void remove(Constante constante) {
        em.remove(em.merge(constante));
    }

    public Constante find(Object id) {
        return em.find(Constante.class, id);
    }

    public List<Constante> findAll() {
        return em.createQuery("select object(o) from Constante as o").getResultList();
    }

    public Constante findById(Long id) {
        try {
            return (Constante) em.createNamedQuery("Constante.findById").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (Constante) em.createNamedQuery("Constante.findById").setParameter("id", id).getResultList().get(0);
        }
    }

    public Constante findByNombre(String nombre) {
        try {
            Constante constante = getCache().get(nombre);
            if (constante != null) {
                return constante;
            } else {
                Query query = em.createNamedQuery("Constante.findByNombre");
                Constante c = (Constante) query.setParameter("nombre", nombre).getSingleResult();
                getCache().put(nombre, c);
                return c;
            }
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (Constante) em.createNamedQuery("Constante.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
        }
    }


}
