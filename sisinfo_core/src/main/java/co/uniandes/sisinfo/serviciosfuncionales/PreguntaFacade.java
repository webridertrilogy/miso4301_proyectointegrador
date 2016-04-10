/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Pregunta;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrador
 */
@Stateless
public class PreguntaFacade extends AbstractFacade<Pregunta> implements PreguntaFacadeLocal, PreguntaFacadeRemote {

    @PersistenceContext(unitName = "ContactosCrmPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public PreguntaFacade() {
        super(Pregunta.class);
    }

    public Pregunta findById(Long id) {
        try {
            return (Pregunta) em.createNamedQuery("Pregunta.findById").setParameter("id", id).getSingleResult();
        } catch (NonUniqueResultException nure) {
            return (Pregunta) em.createNamedQuery("Pregunta.findById").setParameter("id", id).getResultList().get(0);
        } catch (NoResultException nre) {
            return null;
        }
    }
}
