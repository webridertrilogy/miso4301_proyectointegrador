
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ComandoXML;
import co.uniandes.sisinfo.entities.Incidente;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Hibernate;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class ComandoXMLFacade extends AbstractFacade<ComandoXML> implements ComandoXMLFacadeLocal, ComandoXMLFacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ComandoXMLFacade() {
        super(ComandoXML.class);
    }

     private void hibernateInitialize(ComandoXML i) {
        Hibernate.initialize(i);
    }

    @Override
    public ComandoXML find(Object id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<ComandoXML> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
