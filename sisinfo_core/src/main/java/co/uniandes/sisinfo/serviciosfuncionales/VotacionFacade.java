/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Votacion;
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
public class VotacionFacade implements VotacionFacadeLocal, VotacionFacadeRemote {
	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(Votacion votacion) {
        em.persist(votacion);
    }

    public void edit(Votacion votacion) {
        em.merge(votacion);
    }

    public void remove(Votacion votacion) {
        em.remove(em.merge(votacion));
    }

    public Votacion find(Object id) {
        return em.find(Votacion.class, id);
    }

    public List<Votacion> findAll() {
        return em.createQuery("select object(o) from Votacion as o").getResultList();
    }

    public List<Votacion> findVotacionesActivas() {
        return em.createNamedQuery("Votacion.findVotacionesActivas").getResultList();
    }

    public List<Votacion> findVotacionesPorCorreo(String correo) {
        try{
            return em.createNamedQuery("Votacion.findVotacionesPorCorreo").setParameter("correo", correo).getResultList();
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
