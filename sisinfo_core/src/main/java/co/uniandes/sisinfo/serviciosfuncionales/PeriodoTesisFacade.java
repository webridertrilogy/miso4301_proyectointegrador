/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.PeriodoTesis;
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
public class PeriodoTesisFacade implements PeriodoTesisFacadeLocal, PeriodoTesisFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(PeriodoTesis periodoTesis) {
        em.persist(periodoTesis);
    }

    public void edit(PeriodoTesis periodoTesis) {
        em.merge(periodoTesis);
    }

    public void remove(PeriodoTesis periodoTesis) {
        em.remove(em.merge(periodoTesis));
    }

    public PeriodoTesis find(Object id) {
        return em.find(PeriodoTesis.class, id);
    }

    public List<PeriodoTesis> findAll() {
        return em.createQuery("select object(o) from PeriodoTesis as o").getResultList();
    }

    public List<PeriodoTesis> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from PeriodoTesis as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from PeriodoTesis as o").getSingleResult()).intValue();
    }

    public PeriodoTesis findByPeriodo(String nombre) {
        Query q = em.createNamedQuery("PeriodoTesis.findByPeriodo").setParameter("periodo", nombre);
        try {
            PeriodoTesis temp = (PeriodoTesis) q.getSingleResult();

            return temp;
        } catch (NonUniqueResultException e) {
            return (PeriodoTesis) q.getResultList().get(0);
        } catch (NoResultException e) {
            return null;
        }
    }

    public PeriodoTesis findActual() {
        Query q = em.createNamedQuery("PeriodoTesis.findByActual").setParameter("actual", true);
        try {
            PeriodoTesis temp = (PeriodoTesis) q.getSingleResult();

            return temp;
        } catch (NonUniqueResultException e) {
            return (PeriodoTesis) q.getResultList().get(0);
        } catch (NoResultException e) {
            return null;
        }
    }





}
