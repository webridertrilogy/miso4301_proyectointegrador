package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Laboratorio;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicios fachade de la entidad Laboratorio
 * @author Marcela Morales
 */
@Stateless
public class LaboratorioFacade implements LaboratorioFacadeLocal, LaboratorioFacadeRemote {

    @PersistenceContext(unitName = "ReservasInventarioPU")
    private EntityManager em;

    public void create(Laboratorio entity) {
        em.persist(entity);
    }

    public void edit(Laboratorio entity) {
        em.merge(entity);
    }

    public void remove(Laboratorio entity) {
        em.remove(em.merge(entity));
    }

    public Laboratorio find(Object id) {
        return em.find(Laboratorio.class, id);
    }

    public List<Laboratorio> findAll() {
        return em.createQuery("select object(o) from Laboratorio as o").getResultList();
    }

    public List<Laboratorio> findRange(int[] range) {
        javax.persistence.Query q = em.createQuery("select object(o) from Laboratorio as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Laboratorio as o").getSingleResult()).intValue();
    }

    public Collection<Laboratorio> findLaboratoriosAutorizados(String correo) {
        Query query = em.createNamedQuery("Laboratorio.findLaboratoriosAutorizados").setParameter("correo", correo);
        return query.getResultList();
    }

    public Laboratorio findLaboratorioPorNombre(String nombre) {
        Query query = em.createNamedQuery("Laboratorio.findLaboratorioPorNombre").setParameter("nombre", nombre);
        try {
            return (Laboratorio) query.getSingleResult();
        } catch (NonUniqueResultException nure) {
            return (Laboratorio) query.getResultList().get(0);
        } catch (NoResultException nre){
            return null;
        }
    }

    public Collection<Laboratorio> findLaboratoriosEncargado(String correo) {
        Query query = em.createNamedQuery("Laboratorio.findLaboratoriosEncargado").setParameter("correo", correo);
        return query.getResultList();
    }
      public Collection<Laboratorio> findLaboratoriosActivos() {
        Query query = em.createNamedQuery("Laboratorio.findLaboratoriosActivos");
        return query.getResultList();
    }

    public Collection<Laboratorio> findLaboratoriosActivosYReservables() {
        Query query = em.createNamedQuery("Laboratorio.findLaboratoriosActivosYReservables");
        return query.getResultList();
    }

}
