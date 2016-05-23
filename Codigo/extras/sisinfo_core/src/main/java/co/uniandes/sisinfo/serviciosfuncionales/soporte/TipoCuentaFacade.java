/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.soporte;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.uniandes.sisinfo.entities.datosmaestros.soporte.TipoCuenta;

/**
 *
 * @author Asistente
 */
@Stateless
public class TipoCuentaFacade implements TipoCuentaFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(TipoCuenta tipoCuenta) {
        em.persist(tipoCuenta);
    }

    public void edit(TipoCuenta tipoCuenta) {
        em.merge(tipoCuenta);
    }

    public void remove(TipoCuenta tipoCuenta) {
        em.remove(em.merge(tipoCuenta));
    }

    public TipoCuenta find(Object id) {
        return em.find(TipoCuenta.class, id);
    }

    public List<TipoCuenta> findAll() {
        return em.createQuery("select object(o) from TipoCuenta as o").getResultList();
    }

    public List<TipoCuenta> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from TipoCuenta as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from TipoCuenta as o").getSingleResult()).intValue();
    }

   
    public List<TipoCuenta> findByName(String name) {
        try{
        return (List<TipoCuenta>) em.createNamedQuery("TipoCuenta.findByNombre").setParameter("nombre", name).getResultList();
    }

        catch(NoResultException e)
        {
            return null;
        }
    }
}
