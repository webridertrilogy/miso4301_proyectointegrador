/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.historico.serviciosfuncionales;

import co.uniandes.sisinfo.historico.entities.h_archivo;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author david
 */
@Stateless
public class h_archivoFacade implements h_archivoFacadeLocal, h_archivoFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(h_archivo h_archivo) {
        em.persist(h_archivo);
    }

    public void edit(h_archivo h_archivo) {
        em.merge(h_archivo);
    }

    public void remove(h_archivo h_archivo) {
        em.remove(em.merge(h_archivo));
    }

    public h_archivo find(Object id) {
        return em.find(h_archivo.class, id);
    }

    public List<h_archivo> findAll() {
        return em.createQuery("select object(o) from h_archivo as o").getResultList();
    }

    public List<h_archivo> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from h_archivo as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from h_archivo as o").getSingleResult()).intValue();
    }

    @Override
    public h_archivo findBySeccionAndTipo(String id, String tipo) {

        Query q = em.createNamedQuery("h_archivo.findBySeccionAndTipo");
        try
        {
            q.setParameter("id", Long.valueOf(id));
            q.setParameter("tipo", tipo);
            return (h_archivo) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }catch (NonUniqueResultException e1) {
            return (h_archivo) q.getResultList().get(0);
        }
    }

    public List<String> findPeriodos() {
        Query q = em.createNamedQuery("h_archivo.findPeriodos");
        return q.getResultList();
    }


}
