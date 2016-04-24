/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.UsuarioEventos;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrador
 */
@Stateless
public class UsuarioEventosFacade extends AbstractFacade<UsuarioEventos> implements UsuarioEventosFacadeLocal {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioEventosFacade() {
        super(UsuarioEventos.class);
    }
 public UsuarioEventos findByCorreo(String correo) {
      try{
            return (UsuarioEventos)em.createNamedQuery("UsuarioEventos.findByCorreo").setParameter("correo", correo).getSingleResult();
        }catch(NonUniqueResultException nure){
            return (UsuarioEventos)em.createNamedQuery("UsuarioEventos.findByCorreo").setParameter("correo", correo).getResultList().get(0);
        }catch(NoResultException nre){
            return null;
        }
    }
  public UsuarioEventos findByHash(String hash) {
      try{
            return (UsuarioEventos)em.createNamedQuery("UsuarioEventos.findByHash").setParameter("hash", hash).getSingleResult();
        }catch(NonUniqueResultException nure){
            return (UsuarioEventos)em.createNamedQuery("UsuarioEventos.findByHash").setParameter("hash", hash).getResultList().get(0);
        }catch(NoResultException nre){
            return null;
        }
    }


}
