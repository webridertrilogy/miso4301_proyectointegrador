/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.datosmaestros.DiaCompleto;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Asistente
 */
@Stateless
public class DiaCompletoFacade extends AbstractFacade<DiaCompleto> implements DiaCompletoFacadeLocal, DiaCompletoFacadeRemote {
    @PersistenceContext
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public DiaCompletoFacade() {
        super(DiaCompleto.class);
    }

}
