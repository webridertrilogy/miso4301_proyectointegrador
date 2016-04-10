/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.DirectorioInterfacesPorRol;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Asistente
 */
@Stateless
public class DirectorioInterfacesPorRolFacade extends AbstractFacade<DirectorioInterfacesPorRol> implements DirectorioInterfacesPorRolFacadeLocal, DirectorioInterfacesPorRolFacadeRemote {
    @PersistenceContext(unitName = "ServiciosInfraestructuraPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public DirectorioInterfacesPorRolFacade() {
        super(DirectorioInterfacesPorRol.class);
    }

    public Collection<DirectorioInterfacesPorRol> buscarInterfacesActivasPorRol(String rol) {
        return em.createNamedQuery("DirectorioInterfacesPorRol.findByRol").setParameter("rol", "%"+rol+"%").getResultList();
    }



}
