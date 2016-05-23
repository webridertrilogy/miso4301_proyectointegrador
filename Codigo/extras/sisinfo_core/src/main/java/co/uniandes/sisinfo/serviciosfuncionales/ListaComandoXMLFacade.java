
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.ComandoXML;
import co.uniandes.sisinfo.entities.ListaComandoXML;
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
public class ListaComandoXMLFacade extends AbstractFacade<ListaComandoXML> implements ListaComandoXMLFacadeLocal {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ListaComandoXMLFacade() {
        super(ListaComandoXML.class);
    }

     private void hibernateInitialize(ListaComandoXML i) {
        Hibernate.initialize(i);
    }

    public void create(ComandoXML comandoXML) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void edit(ComandoXML comandoXML) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Elimina un comando de lista de comandos.
     * @param comando
     */
    public void remove(ListaComandoXML comando) {

        em.remove(em.merge(comando));
    }

    /**
     * Consulta comandos por nombre.
     * @return
     */
    public List<ListaComandoXML> findByNombre(String nombre) {

        List <ListaComandoXML> lista =  em.createNamedQuery("ListaComandoXML.findByNombre").setParameter("nombre", nombre).getResultList();
        for (ListaComandoXML listaComandoXML : lista) {

            hibernateInitialize(listaComandoXML);
        }
        return lista;
    }
}
