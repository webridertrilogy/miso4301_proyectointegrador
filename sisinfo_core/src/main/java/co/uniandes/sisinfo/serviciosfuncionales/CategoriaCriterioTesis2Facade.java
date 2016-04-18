/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.CategoriaCriterioTesis2;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class CategoriaCriterioTesis2Facade extends AbstractFacade<CategoriaCriterioTesis2> implements CategoriaCriterioTesis2FacadeLocal, CategoriaCriterioTesis2FacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoriaCriterioTesis2Facade() {
        super(CategoriaCriterioTesis2.class);
    }

}
