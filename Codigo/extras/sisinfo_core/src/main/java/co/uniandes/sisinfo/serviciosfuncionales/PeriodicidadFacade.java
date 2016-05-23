/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.*;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author david
 */
@Stateless
public class PeriodicidadFacade implements PeriodicidadFacadeLocal {
	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(Periodicidad periodicidad) {
        em.persist(periodicidad);
    }

    public void edit(Periodicidad periodicidad) {
        em.merge(periodicidad);
    }

    public void remove(Periodicidad periodicidad) {
        em.remove(em.merge(periodicidad));
    }

    public Periodicidad find(Object id) {
        return em.find(Periodicidad.class, id);
    }

    public List<Periodicidad> findAll() {
        return em.createNamedQuery("Periodicidad.findAll").getResultList();
    }

    public Periodicidad findByNombre(String nombre) {
        try{
            return (Periodicidad)em.createNamedQuery("Periodicidad.findByNombre").setParameter("nombre", nombre).getSingleResult();
        }catch(NonUniqueResultException nure){
            return (Periodicidad)em.createNamedQuery("Periodicidad.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
        }catch(NoResultException nre){
            return null;
        }
    }

}
