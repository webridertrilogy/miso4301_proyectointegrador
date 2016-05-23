/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Votante;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author da-naran
 */
@Stateless
public class VotanteFacade implements VotanteFacadeLocal {
	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(Votante votante) {
        em.persist(votante);
    }

    public void edit(Votante votante) {
        em.merge(votante);
    }

    public void remove(Votante votante) {
        em.remove(em.merge(votante));
    }

    public Votante find(Object id) {
        return em.find(Votante.class, id);
    }

    public List<Votante> findAll() {
        return em.createQuery("select object(o) from Votante as o").getResultList();
    }

    public List<Votante> findSinVotar() {
        return em.createNamedQuery("Votante.findSinVotar").getResultList();
    }

    public List<Votante> findByCorreo(String correo) {
        try{
            return em.createNamedQuery("Votante.findByCorreo").setParameter("correo", correo).getResultList();
        }catch(NoResultException nre){
            return null;
        }
    }

    public Votante findByCorreoYIDVotacion(String correo, String idVot) {

        Query q = em.createNamedQuery("Votante.findByCorreoYIDVotacion");
        q = q.setParameter("correo", correo);
        q = q.setParameter("idVotacion", Long.parseLong(idVot));
        try {
            return (Votante) q.getSingleResult();
        } catch (NonUniqueResultException e) {
            return (Votante) q.getResultList().get(0);
        }
        catch (Exception e) {
            return null;
        }
    }

    public List<Votante> findByIdVotacion(String idVotacion) {
        try
        {
            Query q = em.createNamedQuery("Votante.findByIdVotacion");
            q = q.setParameter("idVotacion", Long.parseLong(idVotacion));
            return q.getResultList();
        }
        catch(NoResultException nre)
        {
            return null;
        }
    }


}
