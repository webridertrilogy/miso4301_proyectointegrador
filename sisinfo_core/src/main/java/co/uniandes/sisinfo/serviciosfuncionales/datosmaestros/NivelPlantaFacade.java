/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.NivelPlanta;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class NivelPlantaFacade implements NivelPlantaFacadeLocal, NivelPlantaFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(NivelPlanta nivelPlanta) {
        em.persist(nivelPlanta);
    }

    public void edit(NivelPlanta nivelPlanta) {
        em.merge(nivelPlanta);
    }

    public void remove(NivelPlanta nivelPlanta) {
        em.remove(em.merge(nivelPlanta));
    }

    public NivelPlanta find(Object id) {
        return em.find(NivelPlanta.class, id);
    }

    public List<NivelPlanta> findAll() {
        return em.createQuery("select object(o) from NivelPlanta as o").getResultList();
    }

    public List<NivelPlanta> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from NivelPlanta as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from NivelPlanta as o").getSingleResult()).intValue();
    }

    public NivelPlanta findByNombre(String name) {
        try{
            return (NivelPlanta)em.createNamedQuery("NivelPlanta.findByNombre").setParameter("nombre", name).getSingleResult();
        }catch(NoResultException nre){
            return null;
        }catch(NonUniqueResultException nure){
            return (NivelPlanta)em.createNamedQuery("NivelPlanta.findByNombre").setParameter("nombre", name).getResultList().get(0);
        }
    }


}
