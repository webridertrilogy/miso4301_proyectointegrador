package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Reserva;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicios fachada de la entidad Reserva
 * @author German Florez, Marcela Morales
 */
@Stateless
public class ReservaFacade implements ReservaFacadeLocal, ReservaFacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(Reserva Reserva) {
        em.persist(Reserva);
    }

    public void edit(Reserva Reserva) {
        em.merge(Reserva);
    }

    public void remove(Reserva Reserva) {
        em.remove(em.merge(Reserva));
    }

    public Reserva find(Object id) {
        return em.find(Reserva.class, id);
    }

    public List<Reserva> findAll() {
        Query q = em.createNamedQuery("Reserva.findAll");
        Date hoy = new Date();
        Date inicio = new Date();
        Date fin = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(hoy);
        c.add(Calendar.DAY_OF_MONTH, -15);
        inicio=c.getTime();
        c.setTime(hoy);
        c.add(Calendar.MONTH,6);
        fin=c.getTime();
        q = q.setParameter("inicio", inicio);
        q = q.setParameter("fin", fin);
        return q.getResultList();
    }

    public List<Reserva> buscarReservasPorLogin(String correo) {
        return (List<Reserva>) em.createNamedQuery("Reserva.findLikeCorreo").setParameter("correo", correo + "%").getResultList();
    }

    public List<Reserva> buscarReservasActualesPorLogin(String correo) {
        Timestamp t = new Timestamp(new Date().getTime());
        Query q = em.createNamedQuery("Reserva.findLikeCorreoYFecha");
        q.setParameter("correo", correo + "%");
        q.setParameter("fecha", t);
        return q.getResultList();
    }

    public List<Reserva> buscarReservasPorCorreoYEstado(String correo, String estado) {
        Query q = em.createNamedQuery("Reserva.findLikeCorreoYEstado");
        q.setParameter("correo", correo + "%");
        q.setParameter("estado", estado);
        return q.getResultList();
    }



    public List<Reserva> buscarReservasFecha(Timestamp fecha) {
        return em.createNamedQuery("Reserva.findByFecha").setParameter("fecha", fecha).getResultList();
    }

    public List<Reserva> buscarReservasRango(Timestamp inicio, Timestamp fin) {
        Query q = em.createNamedQuery("Reserva.findByRango");
        q = q.setParameter("inicio", inicio);
        q = q.setParameter("fin", fin);
        return q.getResultList();
    }

    public Reserva buscarReservasInterseccionHoras(Timestamp fecha, Timestamp inicio, Timestamp fin) {
        Query q = em.createNamedQuery("Reserva.findByInterseccionHoras");
        q = q.setParameter("fecha", fecha);
        q = q.setParameter("inicio", inicio);
        q = q.setParameter("fin", fin);
        try {
            return (Reserva) q.getSingleResult();
        } catch (NonUniqueResultException nure) {
            return (Reserva) q.getResultList().get(0);
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<Reserva> buscarReservasPorEstado(String estado) {
        Query q = em.createNamedQuery("Reserva.findByEstado");
        q.setParameter("estado", estado);
        return q.getResultList();
    }
}
