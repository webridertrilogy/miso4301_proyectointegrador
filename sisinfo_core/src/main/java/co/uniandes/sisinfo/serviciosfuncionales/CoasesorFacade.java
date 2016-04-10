/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Coasesor;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class CoasesorFacade extends AbstractFacade<Coasesor> implements CoasesorFacadeLocal, CoasesorFacadeRemote {
    @PersistenceContext(unitName = "TesisMaestriaPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public CoasesorFacade() {
        super(Coasesor.class);
    }

    public Coasesor findByCorreo(String correo) {
       Query q = em.createNamedQuery("Coasesor.findByCorreo").setParameter("correo", correo);
        try {
            Coasesor temp = (Coasesor) q.getSingleResult();
            return temp;
        } catch (NonUniqueResultException e) {
            return (Coasesor) q.getResultList().get(0);
        } catch (NoResultException e) {
            return null;
        }
    }



}
