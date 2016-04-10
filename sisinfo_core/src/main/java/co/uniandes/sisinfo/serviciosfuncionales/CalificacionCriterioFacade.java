/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CalificacionCriterio;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class CalificacionCriterioFacade extends AbstractFacade<CalificacionCriterio> implements CalificacionCriterioFacadeLocal, CalificacionCriterioFacadeRemote {
    @PersistenceContext(unitName = "TesisMaestriaPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public CalificacionCriterioFacade() {
        super(CalificacionCriterio.class);
    }

}
