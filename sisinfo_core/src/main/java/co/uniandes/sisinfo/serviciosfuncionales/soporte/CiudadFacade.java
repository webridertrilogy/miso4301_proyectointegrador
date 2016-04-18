/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales.soporte;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.uniandes.sisinfo.entities.datosmaestros.soporte.Ciudad;

/**
 *
 * @author Administrador
 */
@Stateless
public class CiudadFacade extends AbstractFacade<Ciudad> implements CiudadFacadeLocal, CiudadFacadeRemote {

    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public Collection<Ciudad> findCiudadesByNombre(String nombre) {
        List<Ciudad> temp =  em.createNamedQuery("Ciudad.findCiudadesByNombre").setParameter("nombre", nombre).getResultList();
        return temp;
    }



    public CiudadFacade() {
        super(Ciudad.class);
    }
}
