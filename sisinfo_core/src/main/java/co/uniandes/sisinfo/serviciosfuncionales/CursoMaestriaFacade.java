/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CursoMaestria;
import java.util.Collection;
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
public class CursoMaestriaFacade implements CursoMaestriaFacadeLocal, CursoMaestriaFacadeRemote {

    @PersistenceContext(unitName = "TesisMaestriaPU")
    private EntityManager em;

    public void create(CursoMaestria cursoMaestria) {
        em.persist(cursoMaestria);
    }

    public void edit(CursoMaestria cursoMaestria) {
        em.merge(cursoMaestria);
    }

    public void remove(CursoMaestria cursoMaestria) {
        em.remove(em.merge(cursoMaestria));
    }

    public CursoMaestria find(Object id) {
        return em.find(CursoMaestria.class, id);
    }

    public List<CursoMaestria> findAll() {
        return em.createQuery("select object(o) from CursoMaestria as o order by o.nombre").getResultList();
    }

    public List<CursoMaestria> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from CursoMaestria as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from CursoMaestria as o").getSingleResult()).intValue();
    }

    public CursoMaestria findByNombre(String nombre) {
        Query q = em.createNamedQuery("CursoMaestria.findByNombre").setParameter("nombre", nombre);
        try {
            CursoMaestria temp = (CursoMaestria) q.getSingleResult();

            return temp;
        } catch (NonUniqueResultException e) {
            return (CursoMaestria) q.getResultList().get(0);
        } catch (NoResultException e) {
            return null;
        }
    }

    public Collection<CursoMaestria> findByClasificacion(String clasificacion) {
        Query q = em.createNamedQuery("CursoMaestria.findByClasificacion").setParameter("clasificacion", clasificacion);

        Collection<CursoMaestria> temp = q.getResultList();

        return temp;

    }
}
