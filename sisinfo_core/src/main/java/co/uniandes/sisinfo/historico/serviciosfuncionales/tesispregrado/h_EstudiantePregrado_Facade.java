package co.uniandes.sisinfo.historico.serviciosfuncionales.tesispregrado;

import co.uniandes.sisinfo.historico.entities.tesispregrado.h_estudiante_pregrado;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicios Fachada - Histórico de Estudiante Pregrado
 * @author Paola Gómez
 */
@Stateless
public class h_EstudiantePregrado_Facade implements h_EstudiantePregrado_FacadeLocal, h_EstudiantePregrado_FacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(h_estudiante_pregrado h_estudiante_pregrado) {
        em.persist(h_estudiante_pregrado);
    }

    public void edit(h_estudiante_pregrado h_estudiante_pregrado) {
        em.merge(h_estudiante_pregrado);
    }

    public void remove(h_estudiante_pregrado h_estudiante_pregrado) {
        em.remove(em.merge(h_estudiante_pregrado));
    }

    public h_estudiante_pregrado find(Object id) {
        return em.find(h_estudiante_pregrado.class, id);
    }

    public List<h_estudiante_pregrado> findAll() {
        return em.createQuery("select object(o) from h_estudiante_pregrado as o").getResultList();
    }

    public List<h_estudiante_pregrado> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_estudiante_pregrado as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_estudiante_pregrado as o").getSingleResult()).intValue();
    }

    public h_estudiante_pregrado findByCorreoEstudiante(String correo) {
        try {
            h_estudiante_pregrado est = (h_estudiante_pregrado) em.createNamedQuery("h_estudiante_pregrado.findByCorreoEstudiante").setParameter("correo", correo).getSingleResult();
            return est;
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<h_estudiante_pregrado> findByCorreoProfesor(String correo) {
        try {
            return (List<h_estudiante_pregrado>) em.createNamedQuery("h_estudiante_pregrado.findByCorreoProfesor").setParameter("correo", correo).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
