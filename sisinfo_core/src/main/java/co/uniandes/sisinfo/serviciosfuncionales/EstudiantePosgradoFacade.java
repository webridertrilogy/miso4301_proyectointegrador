package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.EstudiantePosgrado;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;

/**
 * Servicios fachada de la entidad EstudiantePosgrado
 * @author Marcela Morales
 */
@Stateless
public class EstudiantePosgradoFacade implements EstudiantePosgradoFacadeLocal, EstudiantePosgradoFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(EstudiantePosgrado estudiantePosgrado) {
        em.persist(estudiantePosgrado);
    }

    public void edit(EstudiantePosgrado estudiantePosgrado) {
        em.merge(estudiantePosgrado);
    }

    public void remove(EstudiantePosgrado estudiantePosgrado) {
        em.remove(em.merge(estudiantePosgrado));
    }

    public EstudiantePosgrado find(Object id) {
        EstudiantePosgrado e= em.find(EstudiantePosgrado.class, id);
        hibernateInitialize(e);
        return e;
    }

    public List<EstudiantePosgrado> findAll() {
        List<EstudiantePosgrado>list= em.createQuery("select object(o) from EstudiantePosgrado as o").getResultList();
        for (EstudiantePosgrado e : list) {
           hibernateInitialize(e);
        }
        return list;
    }

    public List<EstudiantePosgrado> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from EstudiantePosgrado as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        List<EstudiantePosgrado>list= q.getResultList();
        for (EstudiantePosgrado e : list) {
           hibernateInitialize(e);
        }
        return list;
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from EstudiantePosgrado as o").getSingleResult()).intValue();
    }

    public EstudiantePosgrado findByCorreo(String correo) {
        try{
            EstudiantePosgrado e = (EstudiantePosgrado) em.createNamedQuery("EstudiantePosgrado.findByCorreo").setParameter("correo", correo).getSingleResult();
            hibernateInitialize(e);
            return e;
        }catch(NoResultException e) {
            return null;
        }
    }

     private void hibernateInitialize(EstudiantePosgrado e) {
        Hibernate.initialize(e);
        Hibernate.initialize(e.getEstudiante());
        if(e.getEstudiante() != null)
            Hibernate.initialize(e.getEstudiante().getPersona());
        Hibernate.initialize(e.getHojaVida());
    }
}
