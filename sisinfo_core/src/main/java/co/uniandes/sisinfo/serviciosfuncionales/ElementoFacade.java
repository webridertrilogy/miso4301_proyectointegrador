package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Elemento;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Servicios fachade de la entidad Elemento
 * @author Marcela Morales
 */
@Stateless
public class ElementoFacade implements ElementoFacadeLocal, ElementoFacadeRemote {
    @PersistenceContext(unitName = "ReservasInventarioPU")
    private EntityManager em;

    public void create(Elemento entity) {
        em.persist(entity);
    }

    public void edit(Elemento entity) {
        em.merge(entity);
    }

    public void remove(Elemento entity) {
        em.remove(em.merge(entity));
    }

    public Elemento find(Object id) {
        return em.find(Elemento.class, id);
    }

    public List<Elemento> findAll() {
        return em.createQuery("select object(o) from Elemento as o").getResultList();
    }

    public List<Elemento> findRange(int[] range) {
        javax.persistence.Query q = em.createQuery("select object(o) from Elemento as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Elemento as o").getSingleResult()).intValue();
    }
}
