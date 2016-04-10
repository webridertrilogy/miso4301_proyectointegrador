/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.TipoCampo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrador
 */
@Stateless
public class TipoCampoFacade extends AbstractFacade<TipoCampo> implements TipoCampoFacadeLocal, TipoCampoFacadeRemote {
    @PersistenceContext(unitName = "ContactosCrmPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoCampoFacade() {
        super(TipoCampo.class);
    }

}
