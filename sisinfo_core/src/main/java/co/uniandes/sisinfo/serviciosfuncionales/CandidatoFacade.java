/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Candidato;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author da-naran
 */
@Stateless
public class CandidatoFacade implements CandidatoFacadeLocal {
	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(Candidato candidato) {
        em.persist(candidato);
    }

    public void edit(Candidato candidato) {
        em.merge(candidato);
    }

    public void remove(Candidato candidato) {
        em.remove(em.merge(candidato));
    }

    public Candidato find(Object id) {
        return em.find(Candidato.class, id);
    }

    public List<Candidato> findAll() {
        return em.createQuery("select object(o) from Candidato as o").getResultList();
    }

    public Candidato findByCorreoYIDVotacion(String correo, String idVot) {
        Query q = em.createNamedQuery("Candidato.findByCorreoYIDVotacion");
        q = q.setParameter("correo", correo);
        q = q.setParameter("idVotacion", Long.parseLong(idVot));
        try {
            return (Candidato) q.getSingleResult();
        } catch (NonUniqueResultException e) {
            return (Candidato) q.getResultList().get(0);
        }
    }

    public Candidato findVotoEnBlancoYIDVotacion(String idVot) {
        Query q = em.createNamedQuery("Candidato.findVotoEnBlancoYIDVotacion");
        q = q.setParameter("idVotacion", Long.parseLong(idVot));
        try {
            return (Candidato) q.getSingleResult();
        } catch (NonUniqueResultException e) {
            return (Candidato) q.getResultList().get(0);
        }
    }

}
