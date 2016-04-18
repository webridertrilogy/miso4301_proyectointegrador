/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.DescargaProfesor;
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
public class DescargaProfesorFacade implements DescargaProfesorFacadeLocal, DescargaProfesorFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(DescargaProfesor descargaProfesor) {
        em.persist(descargaProfesor);
    }

    public void edit(DescargaProfesor descargaProfesor) {
        em.merge(descargaProfesor);
    }

    public void remove(DescargaProfesor descargaProfesor) {
        em.remove(em.merge(descargaProfesor));
    }

    public DescargaProfesor find(Object id) {
        return em.find(DescargaProfesor.class, id);
    }

    public List<DescargaProfesor> findAll() {
        return em.createQuery("select object(o) from DescargaProfesor as o").getResultList();
    }

    public List<DescargaProfesor> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from DescargaProfesor as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from DescargaProfesor as o").getSingleResult()).intValue();
    }

    public DescargaProfesor findByNombre(String nombre) {
        try {
            return (DescargaProfesor) em.createNamedQuery("DescargaProfesor.findByNombre").setParameter("nombre", nombre).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (DescargaProfesor) em.createNamedQuery("DescargaProfesor.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
        }
    }






}
