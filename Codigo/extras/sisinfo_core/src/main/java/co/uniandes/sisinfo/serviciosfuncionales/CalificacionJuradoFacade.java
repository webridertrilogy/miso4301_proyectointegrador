/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.CalificacionCriterio;
import co.uniandes.sisinfo.entities.CalificacionJurado;
import co.uniandes.sisinfo.entities.CategoriaCriterioJurado;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.hibernate.Hibernate;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class CalificacionJuradoFacade extends AbstractFacade<CalificacionJurado> implements CalificacionJuradoFacadeLocal {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public CalificacionJuradoFacade() {
        super(CalificacionJurado.class);
    }

    public CalificacionJurado findByHash(String hash) {
        Query q = em.createNamedQuery("CalificacionJurado.findByHash").setParameter("hash", hash);
        try {
            CalificacionJurado temp = (CalificacionJurado) q.getSingleResult();
            hibernateInitialize(temp);
            return temp;
        } catch (NonUniqueResultException e) {
            return (CalificacionJurado) q.getResultList().get(0);
        } catch (NoResultException e) {
            return null;
        }
    }

    private void hibernateInitialize(CalificacionJurado temp) {
        Hibernate.initialize(temp);
        Collection<CategoriaCriterioJurado> calCriterios = temp.getCategoriaCriteriosJurado();
        if (calCriterios != null) {
            for (CategoriaCriterioJurado categoriaCriterioJurado : calCriterios) {
                Hibernate.initialize(categoriaCriterioJurado);
                   Collection<CalificacionCriterio> clasCrit = categoriaCriterioJurado.getCalCriterios();
                   for (CalificacionCriterio calificacionCriterio : clasCrit) {
                      Hibernate.initialize(calificacionCriterio);
                }
            }
        }
        
        if (temp.getJuradoExterno() != null) {
            Hibernate.initialize(temp.getJuradoExterno());
        }
        if (temp.getJuradoInterno() != null) {
            Hibernate.initialize(temp.getJuradoInterno());
            Hibernate.initialize(temp.getJuradoInterno().getPersona());
        }


        if (temp.getTesisCalificada() != null) {
            Hibernate.initialize(temp.getTesisCalificada());
            Hibernate.initialize(temp.getTesisCalificada().getEstudiante());
            Hibernate.initialize(temp.getTesisCalificada().getEstudiante().getPersona());
        }
    }
}
