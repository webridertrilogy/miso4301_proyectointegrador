/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ReservaInventario;
import co.uniandes.sisinfo.entities.ReservaMultiple;
import java.util.Collection;
import java.util.Date;
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
public class ReservaMultipleFacade extends AbstractFacade<ReservaMultiple> implements ReservaMultipleFacadeLocal, ReservaMultipleFacadeRemote {

    @PersistenceContext(unitName = "ReservasInventarioPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public List<ReservaMultiple> findAll() {
        return em.createQuery("ReservaMultiple.findAll").getResultList();
    }

    public ReservaMultiple findById(Long id) {
        try {
            Query query = em.createNamedQuery("ReservaMultiple.findById").setParameter("id", id);
            return (ReservaMultiple) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            ReservaMultiple reservaMultiple = (ReservaMultiple) em.createNamedQuery("ReservaMultiple.findById").setParameter("id", id).getResultList().get(0);
            return reservaMultiple;
        }
    }

    public Collection<ReservaInventario> consultarReservasMultiplesPorPeriodicidad(String periodicidad) {
        Query query = em.createNamedQuery("ReservaMultiple.findReservaMultipleByPeriodicidad").setParameter("periodicidad", periodicidad);
        return query.getResultList();
    }

    public Collection<ReservaInventario> consultarReservasMultiplesPorFinalizacionReservaMultiple(Date finalizacionReservaMultiple) {
        Query query = em.createNamedQuery("ReservaMultiple.findReservaMultipleByFinalizacionReservaMultiple").setParameter("finalizacionReservaMultiple", finalizacionReservaMultiple);
        return query.getResultList();
    }

    public ReservaMultiple consultarReservasMultiplesPorReserva(Long id) {
        try {
            Query query = em.createNamedQuery("ReservaMultiple.findReservaMultipleByReserva").setParameter("id", id);
            return (ReservaMultiple) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            ReservaMultiple reservaMultiple = (ReservaMultiple) em.createNamedQuery("ReservaMultiple.findReservaMultipleByReserva").setParameter("id", id).getResultList().get(0);
            return reservaMultiple;
        }
    }

    public ReservaMultipleFacade() {
        super(ReservaMultiple.class);
    }
}
