/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.InscripcionEventoExterno;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrador
 */
@Stateless
public class InscripcionEventoExternoFacade extends AbstractFacade<InscripcionEventoExterno> implements InscripcionEventoExternoFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public InscripcionEventoExternoFacade() {
        super(InscripcionEventoExterno.class);
    }

    public Collection<InscripcionEventoExterno> findInscripcionesByIdContacto(Long id) {
        List<InscripcionEventoExterno> inscripciones= em.createNamedQuery("InscripcionEventoExterno.findInscripcionesByIdContacto").setParameter("id", id).getResultList();
        return inscripciones;
    }





}
