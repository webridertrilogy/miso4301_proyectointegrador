/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.ListaNegraReservaCitas;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Asistente
 */
@Stateless
public class ListaNegraReservaCitasFacade extends AbstractFacade<ListaNegraReservaCitas> implements ListaNegraReservaCitasFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ListaNegraReservaCitasFacade() {
        super(ListaNegraReservaCitas.class);
    }

    public ListaNegraReservaCitas findByCorreo(String correo) {
        Query q = em.createNamedQuery("ListaNegraReservaCitas.findByCorreo").setParameter("correo", correo);
        try{
            return (ListaNegraReservaCitas)q.getSingleResult();
        }catch(NonUniqueResultException nure){
            return (ListaNegraReservaCitas)q.getResultList().get(0);
        }catch(NoResultException nre){
            return null;
        }
    }





}
