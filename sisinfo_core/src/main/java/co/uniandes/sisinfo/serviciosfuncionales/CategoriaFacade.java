package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Categoria;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Servicios fachade de la entidad Categoria
 * @author Marcela Morales
 */
@Stateless
public class CategoriaFacade implements CategoriaFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(Categoria entity) {
        em.persist(entity);
    }

    public void edit(Categoria entity) {
        em.merge(entity);
    }

    public void remove(Categoria entity) {
        em.remove(em.merge(entity));
    }

    public Categoria find(Object id) {
        return em.find(Categoria.class, id);
    }

    public List<Categoria> findAll() {
        return em.createQuery("select object(o) from Categoria as o").getResultList();
    }

    public List<Categoria> findRange(int[] range) {
        javax.persistence.Query q = em.createQuery("select object(o) from Categoria as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from Categoria as o").getSingleResult()).intValue();
    }
}
