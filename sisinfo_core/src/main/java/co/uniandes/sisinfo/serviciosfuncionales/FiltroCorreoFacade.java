/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.FiltroCorreo;

/**
 *
 * @author Asistente
 */
@Stateless
public class FiltroCorreoFacade extends AbstractFacade<FiltroCorreo> implements FiltroCorreoFacadeLocal, FiltroCorreoFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public FiltroCorreoFacade() {
        super(FiltroCorreo.class);
    }



}
