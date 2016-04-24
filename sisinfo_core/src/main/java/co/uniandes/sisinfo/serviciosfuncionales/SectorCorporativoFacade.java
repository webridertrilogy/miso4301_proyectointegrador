/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.SectorCorporativo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ivan Melo
 */
@Stateless
public class SectorCorporativoFacade implements SectorCorporativoFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(SectorCorporativo sectorCorporativo) {
        em.persist(sectorCorporativo);
    }

    public void edit(SectorCorporativo sectorCorporativo) {
        em.merge(sectorCorporativo);
    }

    public void remove(SectorCorporativo sectorCorporativo) {
        em.remove(em.merge(sectorCorporativo));
    }

    public SectorCorporativo find(Object id) {
        return em.find(SectorCorporativo.class, id);
    }

    public List<SectorCorporativo> findAll() {
        return em.createQuery("select object(o) from SectorCorporativo as o").getResultList();
    }

    public List<SectorCorporativo> findRange(int[] range) {
        Query q = em.createQuery("select object(o) from SectorCorporativo as o");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        return ((Long) em.createQuery("select count(o) from SectorCorporativo as o").getSingleResult()).intValue();
    }

    public SectorCorporativo findByNombre(String nombre) {
         try{
            return (SectorCorporativo)em.createNamedQuery("SectorCorporativo.findByNombre").setParameter("nombre", nombre).getSingleResult();
        }catch(NonUniqueResultException nure){
            return (SectorCorporativo)em.createNamedQuery("SectorCorporativo.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
        }catch(NoResultException nre){
            return null;
        }
    }



}
