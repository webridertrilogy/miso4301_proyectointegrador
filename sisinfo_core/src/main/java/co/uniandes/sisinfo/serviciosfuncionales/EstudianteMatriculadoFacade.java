package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.EstudianteMatriculado;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicios Fachada de la entidad EstudianteMatriculado
 * @author Marcela Morales
 */
@Stateless
public class EstudianteMatriculadoFacade implements EstudianteMatriculadoFacadeLocal {

    //----------------------------------------------
    // ATRIBUTOS
    //----------------------------------------------
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    //----------------------------------------------
    // MÉTODOS
    //----------------------------------------------
    public void create(EstudianteMatriculado estudiante) {
        em.persist(estudiante);
    }

    public void edit(EstudianteMatriculado estudiante) {
        em.merge(estudiante);
    }

    public void remove(EstudianteMatriculado estudiante) {
        em.remove(em.merge(estudiante));
    }

    public EstudianteMatriculado find(Object id) {
        return em.find(EstudianteMatriculado.class, id);
    }

    public List<EstudianteMatriculado> findAll() {
        return em.createQuery("select object(o) from Estudiante as o").getResultList();
    }

    public List<EstudianteMatriculado> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from Estudiante as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Estudiante as o").getSingleResult()).intValue();
    }

    public EstudianteMatriculado findByCarnet(String carnet) {
        Query q = em.createNamedQuery("EstudianteMatriculado.findByCarnet").setParameter("carnet", carnet);
        try {
            EstudianteMatriculado temp = (EstudianteMatriculado) q.getSingleResult();
            return temp;
        } catch (NonUniqueResultException e) {
            return (EstudianteMatriculado) q.getResultList().get(0);
        } catch (NoResultException e) {
            return null;
        }
    }
}
