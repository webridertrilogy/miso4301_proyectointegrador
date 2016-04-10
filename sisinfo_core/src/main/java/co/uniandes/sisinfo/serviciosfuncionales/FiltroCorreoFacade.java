/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.FiltroCorreo;
import co.uniandes.sisinfo.entities.FiltroCorreo;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Asistente
 */
@Stateless
public class FiltroCorreoFacade extends AbstractFacade<FiltroCorreo> implements FiltroCorreoFacadeLocal, FiltroCorreoFacadeRemote {
    @PersistenceContext(unitName = "ServiciosInfraestructuraPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public FiltroCorreoFacade() {
        super(FiltroCorreo.class);
    }



}
