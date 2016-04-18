/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;
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
public class NivelFormacionFacade implements NivelFormacionFacadeLocal, NivelFormacionFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(NivelFormacion nivelFormacion) {
        em.persist(nivelFormacion);
    }

    public void edit(NivelFormacion nivelFormacion) {
        em.merge(nivelFormacion);
    }

    public void remove(NivelFormacion nivelFormacion) {
        em.remove(em.merge(nivelFormacion));
    }

    public NivelFormacion find(Object id) {
        return em.find(NivelFormacion.class, id);
    }

    public List<NivelFormacion> findAll() {
        return em.createQuery("select object(o) from NivelFormacion as o").getResultList();
    }

    public List<NivelFormacion> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from NivelFormacion as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from NivelFormacion as o").getSingleResult()).intValue();
    }

    public NivelFormacion findByName(String name) {
        try{
            return (NivelFormacion)em.createNamedQuery("NivelFormacion.findByName").setParameter("nombre", name).getSingleResult();
        }catch(NoResultException nre){
            return null;
        }catch(NonUniqueResultException nure){
            return (NivelFormacion)em.createNamedQuery("NivelFormacion.findByName").setParameter("nombre", name).getResultList().get(0);
        }
    }



}
