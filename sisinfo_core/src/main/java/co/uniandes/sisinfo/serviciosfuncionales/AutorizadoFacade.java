/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.Autorizado;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Admin
 */
@Stateless
public class AutorizadoFacade extends AbstractFacade<Autorizado> implements AutorizadoFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public AutorizadoFacade() {
        super(Autorizado.class);
    }


    @Override
     public void create(Autorizado entity) {
        em.persist(entity);
    }

    @Override
    public void edit(Autorizado entity) {
        em.merge(entity);
    }

    @Override
    public void remove(Autorizado entity) {
        em.remove(em.merge(entity));
    }

    @Override
    public Autorizado find(Object id) {
        return em.find(Autorizado.class, id);
    }

    @Override
    public List<Autorizado> findAll() {
        return em.createQuery("select object(o) from Autorizado as o").getResultList();
    }

    @Override
    public List<Autorizado> findRange(int[] range) {
        javax.persistence.Query q = em.createQuery("select object(o) from Autorizado as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    @Override
    public int count() {
        return ((Long) em.createQuery("select count(o) from Autorizado as o").getSingleResult()).intValue();
    } 

    public Autorizado findAutorizadoByCorreo(String correo) {
        Query q=em.createNamedQuery("Autorizado.findAutorizadoByCorreo").setParameter("correo", correo);
        try {
            return (Autorizado)q.getSingleResult();
        } catch (Exception e) {
            return (Autorizado)q.getResultList().get(0);
        }
    }

}
