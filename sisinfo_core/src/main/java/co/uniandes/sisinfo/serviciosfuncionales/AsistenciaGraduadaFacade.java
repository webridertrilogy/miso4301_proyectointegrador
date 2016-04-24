package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.AsistenciaGraduada;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicios fachada de la entidad AsistenciaGraduada
 * @author Marcela Morales
 */
@Stateless
public class AsistenciaGraduadaFacade implements AsistenciaGraduadaFacadeLocal {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(AsistenciaGraduada AsistenciaGraduada) {
        em.persist(AsistenciaGraduada);
    }

    public void edit(AsistenciaGraduada AsistenciaGraduada) {
        em.merge(AsistenciaGraduada);
    }

    public void remove(AsistenciaGraduada AsistenciaGraduada) {
        em.remove(em.merge(AsistenciaGraduada));
    }

    public AsistenciaGraduada find(Object id) {
        return em.find(AsistenciaGraduada.class, id);
    }

    public List<AsistenciaGraduada> findAll() {
        return em.createQuery("select object(o) from AsistenciaGraduada as o").getResultList();
    }

    public AsistenciaGraduada findById(Long id) {
        try {
            return (AsistenciaGraduada) em.createNamedQuery("AsistenciaGraduada.findById").setParameter("id", id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return (AsistenciaGraduada) em.createNamedQuery("AsistenciaGraduada.findById").setParameter("id", id).getResultList().get(0);
        }
    }

    public List<AsistenciaGraduada> findByPeriodo(String periodo) {
        return (List<AsistenciaGraduada>) em.createNamedQuery("AsistenciaGraduada.findByPeriodo").setParameter("periodo", periodo).getResultList();
    }

    public List<AsistenciaGraduada> findByCorreoProfesor(String correoProfesor) {
        return (List<AsistenciaGraduada>) em.createNamedQuery("AsistenciaGraduada.findByCorreoProfesor").setParameter("correo", correoProfesor).getResultList();
    }

    public List<AsistenciaGraduada> findByCorreoEstudiante(String correoEstudiante) {
        return (List<AsistenciaGraduada>) em.createNamedQuery("AsistenciaGraduada.findByCorreoEstudiante").setParameter("correo", correoEstudiante).getResultList();
    }

    public AsistenciaGraduada findByPeriodoYCorreoEstudiante(String periodo, String correoEstudiante) {
        try {
            Query query = em.createNamedQuery("AsistenciaGraduada.findByPeriodoYCorreoEstudiante");
            query.setParameter("periodo", periodo);
            query.setParameter("correo", correoEstudiante);
            return (AsistenciaGraduada) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            Query query = em.createNamedQuery("AsistenciaGraduada.findByPeriodoYCorreoEstudiante");
            query.setParameter("periodo", periodo);
            query.setParameter("correo", correoEstudiante);
            return (AsistenciaGraduada) query.getResultList().get(0);
        }
    }
}
