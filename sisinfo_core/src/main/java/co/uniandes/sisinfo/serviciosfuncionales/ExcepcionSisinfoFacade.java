/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.ExcepcionSisinfo;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class ExcepcionSisinfoFacade extends AbstractFacade<ExcepcionSisinfo> implements ExcepcionSisinfoFacadeLocal, ExcepcionSisinfoFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ExcepcionSisinfoFacade() {
        super(ExcepcionSisinfo.class);
    }

    public List<ExcepcionSisinfo> findByComandoFechaMetodo(String comando, String nombreMetodo, Timestamp fecha) {
       List<ExcepcionSisinfo> temp = em.createNamedQuery("ExcepcionSisinfo.findByComandoFechaMetodo").setParameter("comando", comando).setParameter("nombreMetodo", nombreMetodo).setParameter("fecha", fecha).getResultList();

        return temp;
    }

    public List<ExcepcionSisinfo> findByEstado(Boolean estado) {
         List<ExcepcionSisinfo> temp = em.createNamedQuery("ExcepcionSisinfo.findByEstado").setParameter("estado", estado).getResultList();
        return temp;
    }

    public List<ExcepcionSisinfo> findByNoBorradas() {
         List<ExcepcionSisinfo> temp = em.createNamedQuery("ExcepcionSisinfo.findByNoBorradas").getResultList();
        return temp;
    }








    

}
