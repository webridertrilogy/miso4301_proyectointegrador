/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.CategoriaCriterioJurado;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class CategoriaCriterioJuradoFacade extends AbstractFacade<CategoriaCriterioJurado> implements CategoriaCriterioJuradoFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoriaCriterioJuradoFacade() {
        super(CategoriaCriterioJurado.class);
    }



}
