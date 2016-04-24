package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Encargado;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicios fachade de la entidad Encargado
 * @author Marcela Morales
 */
@Stateless
public class EncargadoFacade implements EncargadoFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(Encargado entity) {
        em.persist(entity);
    }

    public void edit(Encargado entity) {
        em.merge(entity);
    }

    public void remove(Encargado entity) {
        em.remove(em.merge(entity));
    }

    public Encargado find(Object id) {
        return em.find(Encargado.class, id);
    }

    public List<Encargado> findAll() {
        return em.createQuery("select object(o) from Encargado as o").getResultList();
    }

    public List<Encargado> findRange(int[] range) {
        javax.persistence.Query q = em.createQuery("select object(o) from Encargado as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Encargado as o").getSingleResult()).intValue();
    }

    public Encargado findByCorreo(String correo) {
        Query q = em.createNamedQuery("Encargado.findByCorreo").setParameter("correo", correo);
        try{
            return (Encargado)q.getSingleResult();
        }catch(NonUniqueResultException e){
            return (Encargado)q.getResultList().get(0);
        }catch(Exception e){
            return null;
        }
    }


}
