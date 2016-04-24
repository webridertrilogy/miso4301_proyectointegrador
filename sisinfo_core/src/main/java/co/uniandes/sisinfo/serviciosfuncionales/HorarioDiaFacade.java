package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.HorarioDia;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Servicios fachade de la entidad HorarioDia
 * @author Marcela Morales
 */
@Stateless
public class HorarioDiaFacade implements HorarioDiaFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(HorarioDia entity) {
        em.persist(entity);
    }

    public void edit(HorarioDia entity) {
        em.merge(entity);
    }

    public void remove(HorarioDia entity) {
        em.remove(em.merge(entity));
    }

    public HorarioDia find(Object id) {
        return em.find(HorarioDia.class, id);
    }

    public List<HorarioDia> findAll() {
        return em.createQuery("select object(o) from HorarioDia as o").getResultList();
    }

    public List<HorarioDia> findRange(int[] range) {
        javax.persistence.Query q = em.createQuery("select object(o) from HorarioDia as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from HorarioDia as o").getSingleResult()).intValue();
    }
}
