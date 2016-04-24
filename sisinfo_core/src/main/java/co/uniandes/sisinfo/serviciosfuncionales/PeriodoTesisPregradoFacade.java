/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.PeriodoTesisPregrado;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.NonUniqueObjectException;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Stateless
public class PeriodoTesisPregradoFacade implements PeriodoTesisPregradoFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(PeriodoTesisPregrado periodoTesisPregrado) {
        em.persist(periodoTesisPregrado);
    }

    public void edit(PeriodoTesisPregrado periodoTesisPregrado) {
        em.merge(periodoTesisPregrado);
    }

    public void remove(PeriodoTesisPregrado periodoTesisPregrado) {
        em.remove(em.merge(periodoTesisPregrado));
    }

    public PeriodoTesisPregrado find(Object id) {
        return em.find(PeriodoTesisPregrado.class, id);
    }

    public List<PeriodoTesisPregrado> findAll() {
        return em.createQuery("select object(o) from PeriodoTesisPregrado as o").getResultList();
    }

    public List<PeriodoTesisPregrado> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from PeriodoTesisPregrado as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from PeriodoTesisPregrado as o").getSingleResult()).intValue();
    }

    public PeriodoTesisPregrado findByPeriodo(String periodo) {
        Query q = em.createNamedQuery("PeriodoTesisPregrado.findByPeriodo").setParameter("periodo", periodo);
        try {
            PeriodoTesisPregrado temp = (PeriodoTesisPregrado) q.getSingleResult();

            return temp;
        } catch (NonUniqueObjectException e) {
            return (PeriodoTesisPregrado) q.getResultList().get(0);
        } catch (NoResultException e) {
            return null;
        }
    }



}
