/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ListaBlancaErroresSisinfo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Stateless
public class ListaBlancaErroresSisinfoFacade extends AbstractFacade<ListaBlancaErroresSisinfo> implements ListaBlancaErroresSisinfoFacadeLocal, ListaBlancaErroresSisinfoFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ListaBlancaErroresSisinfoFacade() {
        super(ListaBlancaErroresSisinfo.class);
    }

    public ListaBlancaErroresSisinfo findByIdError(String idError) {
          try {
            ListaBlancaErroresSisinfo i = (ListaBlancaErroresSisinfo) em.createNamedQuery("ListaBlancaErroresSisinfo.findByidError").setParameter("idError", idError).getSingleResult();

            return i;
        } catch (NonUniqueResultException e) {
            List<ListaBlancaErroresSisinfo> list= em.createNamedQuery("ListaBlancaErroresSisinfo.findByidError").setParameter("idError", idError).getResultList();
            ListaBlancaErroresSisinfo i = list.get(0);

            return i;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Elimina un error de lista blanca.
     * @param listaBlancaErroresSisinfo
     */
    public void remove(ListaBlancaErroresSisinfo listaBlancaErroresSisinfo) {
        
        em.remove(em.merge(listaBlancaErroresSisinfo));
    }
}
