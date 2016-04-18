/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.soporte;


import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.uniandes.sisinfo.entities.datosmaestros.soporte.Pais;

/**
 *
 * @author lj.bautista31
 */
@Stateless
@EJB(name = "PaisFacade", beanInterface =co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeLocal.class)
public class PaisFacade implements PaisFacadeLocal, PaisFacadeRemote {
	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(Pais pais) {
        em.persist(pais);
    }

    @Override
    public void edit(Pais pais) {
        em.merge(pais);
    }

    @Override
    public void remove(Pais pais) {
        em.remove(em.merge(pais));
    }

    @Override
    public Pais find(Object id) {
        return em.find(Pais.class, id);
    }

    @Override
    public List<Pais> findAll() {
        return em.createQuery("select object(o) from Pais as o").getResultList();
    }

    @Override
    public Pais findByNombre(String nombre) {
        Query nq = em.createNamedQuery("Pais.findByNombre");
        nq.setParameter("nombre", nombre);
        try{
            return (Pais)nq.getSingleResult();
        }catch(NoResultException nre){
            return null;
        }catch(NonUniqueResultException nure){
            return (Pais)nq.getResultList().get(0);
        }
        
    }

}
