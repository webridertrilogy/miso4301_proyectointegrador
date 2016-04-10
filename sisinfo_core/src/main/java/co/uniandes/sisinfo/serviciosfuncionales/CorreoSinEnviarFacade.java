/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CorreoSinEnviar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Asistente
 */
@Stateless
public class CorreoSinEnviarFacade extends AbstractFacade<CorreoSinEnviar> implements CorreoSinEnviarFacadeLocal, CorreoSinEnviarFacadeRemote {
    @PersistenceContext(unitName = "ServiciosInfraestructuraPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public CorreoSinEnviarFacade() {
        super(CorreoSinEnviar.class);
    }

    @Override
    public List<CorreoSinEnviar> findAll() {
        return getEntityManager().createQuery("select object(o) from CorreoSinEnviar as o order by fechaEnvio desc").getResultList();
    }

}
