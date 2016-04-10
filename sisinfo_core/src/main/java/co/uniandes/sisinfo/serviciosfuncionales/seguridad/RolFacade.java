/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.seguridad;

import co.uniandes.sisinfo.entities.datosmaestros.Rol;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author lj.bautista31
 */
@Stateless
public class RolFacade implements RolFacadeLocal, RolFacadeRemote {
    @PersistenceContext
    private EntityManager em;

    public void create(Rol rol) {
        em.persist(rol);
    }

    public void edit(Rol rol) {
        em.merge(rol);
    }

    public void remove(Rol rol) {
        em.remove(em.merge(rol));
    }

    public Rol find(Object id) {
        return em.find(Rol.class, id);
    }

    public List<Rol> findAll() {
        return em.createQuery("select object(o) from Rol as o").getResultList();
    }

    public Rol findByRol(String rol) {
        return (Rol)em.createNamedQuery("Rol.findByRol").setParameter("rol", rol).getSingleResult();
    }

    public Rol findByDescripcion(String descripcion) {
        return (Rol)em.createNamedQuery("Rol.findByDescripcion").setParameter("descripcion", descripcion).getSingleResult();
    }

}
