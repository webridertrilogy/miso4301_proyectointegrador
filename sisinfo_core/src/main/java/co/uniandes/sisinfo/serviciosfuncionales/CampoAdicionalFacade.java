/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CampoAdicional;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrador
 */
@Stateless
public class CampoAdicionalFacade extends AbstractFacade<CampoAdicional> implements CampoAdicionalFacadeLocal, CampoAdicionalFacadeRemote {
    @PersistenceContext(unitName = "ContactosCrmPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public CampoAdicionalFacade() {
        super(CampoAdicional.class);
    }

}
