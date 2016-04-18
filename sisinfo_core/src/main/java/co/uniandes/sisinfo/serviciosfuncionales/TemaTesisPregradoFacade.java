/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TemaTesisPregrado;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;
import org.hibernate.NonUniqueObjectException;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Stateless
public class TemaTesisPregradoFacade implements TemaTesisPregradoFacadeLocal, TemaTesisPregradoFacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(TemaTesisPregrado temaTesisPregrado) {
        em.persist(temaTesisPregrado);
    }

    public void edit(TemaTesisPregrado temaTesisPregrado) {
        em.merge(temaTesisPregrado);
    }

    public void remove(TemaTesisPregrado temaTesisPregrado) {
        em.remove(em.merge(temaTesisPregrado));
    }

    public TemaTesisPregrado find(Object id) {
        return em.find(TemaTesisPregrado.class, id);
    }

    public List<TemaTesisPregrado> findAll() {
        return em.createQuery("select object(o) from TemaTesisPregrado as o").getResultList();
    }

    public List<TemaTesisPregrado> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from TemaTesisPregrado as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from TemaTesisPregrado as o").getSingleResult()).intValue();
    }

    public TemaTesisPregrado findByNombreYCorreoAsesor(String nombre, String correo) {
        try {
            TemaTesisPregrado lista = (TemaTesisPregrado) em.createNamedQuery("TemaTesisPregrado.findByNombreTemaTesisYCorreoAsesor").setParameter("nombre", nombre).setParameter("correo", correo).getSingleResult();
            hibernateInitialize(lista);
            return lista;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueObjectException ex) {
            List<TemaTesisPregrado> lista = em.createNamedQuery("TemaTesisPregrado.findByNombreTemaTesisYCorreoAsesor").setParameter("nombre", nombre).getResultList();
            return lista.get(0);
        }
    }


    public TemaTesisPregrado findByNombreYCorreoAsesorYPeriodo(String nombre, String correo, String periodo) {
        try {
            TemaTesisPregrado lista = (TemaTesisPregrado) em.createNamedQuery("TemaTesisPregrado.findByNombreTemaTesisYCorreoAsesorYPeriodo").setParameter("nombre", nombre).setParameter("correo", correo).setParameter("periodo", periodo).getSingleResult();
            hibernateInitialize(lista);
            return lista;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueObjectException ex) {
            List<TemaTesisPregrado> lista = em.createNamedQuery("TemaTesisPregrado.findByNombreTemaTesisYCorreoAsesorYPeriodo").setParameter("nombre", nombre).getResultList();
            return lista.get(0);
        }
    }


    private void hibernateInitialize(TemaTesisPregrado i) {
        Hibernate.initialize(i);
        if (i.getAsesor() != null) {
            Hibernate.initialize(i.getAsesor());
            Hibernate.initialize(i.getAsesor().getPersona());
        }
    }

    public Collection<TemaTesisPregrado> findByCorreoAsesor(String correo) {
        Collection<TemaTesisPregrado> lista = em.createNamedQuery("TemaTesisPregrado.findBycorreoAsesor").setParameter("correoasesor", correo).getResultList();
        for (TemaTesisPregrado temaTesisPregrado : lista) {
            hibernateInitialize(temaTesisPregrado);
        }
        return lista;
    }

    public List<TemaTesisPregrado> findByPeriodoYCorreoAsesor(String correo, String periodo) {
        List<TemaTesisPregrado> lista = em.createNamedQuery("TemaTesisPregrado.findByPeriodoYCorreoAsesor").setParameter("correoasesor", correo).setParameter("periodo", periodo).getResultList();
        for (TemaTesisPregrado temaTesisPregrado : lista) {
            hibernateInitialize(temaTesisPregrado);
        }
        return lista;
    }
}
