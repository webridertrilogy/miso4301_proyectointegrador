/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.CategoriaEventoExterno;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class CategoriaEventoExternoFacade extends AbstractFacade<CategoriaEventoExterno> implements CategoriaEventoExternoFacadeLocal, CategoriaEventoExternoFacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoriaEventoExterno findByNombre(String nombre) {
        Query q = em.createNamedQuery("CategoriaEventoExterno.findByNombre").setParameter("nombre", nombre);
        try {
            CategoriaEventoExterno temp = (CategoriaEventoExterno) q.getSingleResult();
            return temp;
        } catch (NonUniqueResultException e) {
            return (CategoriaEventoExterno) q.getResultList().get(0);
        } catch (NoResultException e) {
            return null;
        }
    }

    public CategoriaEventoExternoFacade() {
        super(CategoriaEventoExterno.class);
    }
}
