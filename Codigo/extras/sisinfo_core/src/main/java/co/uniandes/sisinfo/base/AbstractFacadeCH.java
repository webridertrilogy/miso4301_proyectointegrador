package co.uniandes.sisinfo.base;

import java.util.List;
import javax.persistence.EntityManager;

/**
 * @author German Florez, Marcela Morales
 * Abstract Facade
 */
public abstract class AbstractFacadeCH<T> {

    private Class<T> entityClass;

    public AbstractFacadeCH(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        return getEntityManager().createQuery("select object(o) from " + entityClass.getSimpleName().toLowerCase() + " as o").getResultList();
    }

    public void removeAll(){
        List<T> objs= findAll();
        for (T t : objs) {
            remove(t);
        }
    }

    public List<T> findRange(int[] range) {
        javax.persistence.Query q = getEntityManager().createQuery("select object(o) from " + entityClass.getSimpleName().toLowerCase()  + " as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) getEntityManager().createQuery("select count(o) from " + entityClass.getSimpleName().toLowerCase()  + " as o").getSingleResult()).intValue();
    }
}
