
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.AccionVencida;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class AccionVencidaFacade extends AbstractFacade<AccionVencida> implements AccionVencidaFacadeLocal, AccionVencidaFacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public AccionVencidaFacade() {
        super(AccionVencida.class);
    }

     private void hibernateInitialize(AccionVencida a) {
        Hibernate.initialize(a);
    }

    @Override
    public AccionVencida find(Object id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<AccionVencida> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
