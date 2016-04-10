package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ReservaInventario;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 * Servicios fachade de la entidad ReservaInventario
 * @author Marcela Morales
 */
@Stateless
public class ReservaInventarioFacade implements ReservaInventarioFacadeLocal, ReservaInventarioFacadeRemote {
    @PersistenceContext(unitName = "ReservasInventarioPU")
    private EntityManager em;

    public void create(ReservaInventario entity) {
        em.persist(entity);
    }

    public void edit(ReservaInventario entity) {
        em.merge(entity);
    }

    public void remove(ReservaInventario entity) {
        em.remove(em.merge(entity));
    }

    public ReservaInventario find(Object id) {
        return em.find(ReservaInventario.class, id);
    }

    public List<ReservaInventario> findAll() {
        return em.createQuery("select object(o) from ReservaInventario as o").getResultList();
    }

    public List<ReservaInventario> findRange(int[] range) {
        javax.persistence.Query q = em.createQuery("select object(o) from ReservaInventario as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from ReservaInventario as o").getSingleResult()).intValue();
    }

    public Collection<ReservaInventario> consultarReservasPorNombreLaboratorio(String nombre) {
        Query query = em.createNamedQuery("ReservaInventario.findReservasByNombreLaboratorio").setParameter("nombre", nombre);
        return query.getResultList();
    }

    public Collection<ReservaInventario> consultarReservasPorNombreLaboratorioyEstado(String nombre, String estado) {
        Query query = em.createNamedQuery("ReservaInventario.findReservasByNombreLaboratorioAndEstado").setParameter("nombre", nombre).setParameter("estado", estado);
        return query.getResultList();
    }

    public Collection<ReservaInventario> consultarReservasPorNombreLaboratorioyEstadoyRangoFechas(String nombre, String estado, Timestamp fechaInicial, Timestamp fechaFinal) {
        Query query = em.createNamedQuery("ReservaInventario.findReservasByNombreLaboratorioAndEstadoAndRangoFechas").setParameter("nombre", nombre).setParameter("estado", estado);
        query = query.setParameter("fechaInicial", fechaInicial, TemporalType.TIMESTAMP).setParameter("fechaFinal", fechaFinal,TemporalType.TIMESTAMP);
        return query.getResultList();
    }

    public Collection<ReservaInventario> consultarReservasByPersonaYEstado(String correo, String estado) {
        Query query = em.createNamedQuery("ReservaInventario.findReservasByPersonaAndEstado").setParameter("correo", correo).setParameter("estado", estado);
        return query.getResultList();
    }



    public Collection<ReservaInventario> consultarReservasAnterioresAFechaByEstado(Date fecha, String estado) {
        Query query = em.createNamedQuery("ReservaInventario.findReservasAnterioresAFechaByEstado").setParameter("fecha", fecha).setParameter("estado", estado);
        return query.getResultList();
    }

    public Collection<ReservaInventario> consultarReservasPorEstado(String estado) {
        Query query = em.createNamedQuery("ReservaInventario.findReservasPorEstado").setParameter("estado", estado);
        return query.getResultList();
    }

    public Collection<ReservaInventario> consultarReservasPorRangoFechas(Timestamp fechaInicial, Timestamp fechaFinal) {
        Query query = em.createNamedQuery("ReservaInventario.findReservasByRangoFechas");
        query = query.setParameter("fechaInicial", fechaInicial, TemporalType.TIMESTAMP).setParameter("fechaFinal", fechaFinal,TemporalType.TIMESTAMP);
        return query.getResultList();
    }





}
