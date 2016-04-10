package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TipoAsistenciaGraduada;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 * Servicios fachada de la entidad TipoAsistenciaGraduada
 * @author Marcela Morales
 */
@Stateless
public class TipoAsistenciaGraduadaFacade implements TipoAsistenciaGraduadaFacadeLocal, TipoAsistenciaGraduadaFacadeRemote {

    @PersistenceContext(unitName = "BolsaEmpleoPU")
    private EntityManager em;

    public void create(TipoAsistenciaGraduada tipoAsistenciaGraduada) {
        em.persist(tipoAsistenciaGraduada);
    }

    public void edit(TipoAsistenciaGraduada tipoAsistenciaGraduada) {
        em.merge(tipoAsistenciaGraduada);
    }

    public void remove(TipoAsistenciaGraduada tipoAsistenciaGraduada) {
        em.remove(em.merge(tipoAsistenciaGraduada));
    }

    public TipoAsistenciaGraduada find(Object id) {
        return em.find(TipoAsistenciaGraduada.class, id);
    }

    public List<TipoAsistenciaGraduada> findAll() {
        return em.createQuery("select object(o) from TipoAsistenciaGraduada as o").getResultList();
    }

    public TipoAsistenciaGraduada findById(Long id) {
        try {
            return (TipoAsistenciaGraduada) em.createNamedQuery("TipoAsistenciaGraduada.findById").setParameter("id", id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return (TipoAsistenciaGraduada) em.createNamedQuery("TipoAsistenciaGraduada.findById").setParameter("id", id).getResultList().get(0);
        }
    }

    public TipoAsistenciaGraduada findByTipo(String tipo) {
        try {
            return (TipoAsistenciaGraduada) em.createNamedQuery("TipoAsistenciaGraduada.findByTipo").setParameter("tipo", tipo).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return (TipoAsistenciaGraduada) em.createNamedQuery("TipoAsistenciaGraduada.findByTipo").setParameter("tipo", tipo).getResultList().get(0);
        }
    }
}
