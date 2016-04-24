/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales.seguridad;

import co.uniandes.sisinfo.entities.datosmaestros.Rol;
import co.uniandes.sisinfo.entities.datosmaestros.Usuario;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.Hibernate;

/**
 * Servicios de Usuario
 */
@Stateless
@EJB(name = "UsuarioFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.seguridad.UsuarioFacadeLocal.class)
public class UsuarioFacade implements UsuarioFacadeLocal {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(Usuario usuario) {
        em.persist(usuario);
    }

    @Override
    public void edit(Usuario usuario) {
        em.merge(usuario);
    }

    @Override
    public void remove(Usuario usuario) {
        em.remove(em.merge(usuario));
    }

    @Override
    public Usuario find(Object id) {
        Usuario usuario = em.find(Usuario.class, id);
        Hibernate.initialize(usuario);
        if(usuario.getPersona() != null)
            Hibernate.initialize(usuario.getPersona());
        if(usuario.getRoles() != null){
            Hibernate.initialize(usuario.getRoles());
            for(Rol rol : usuario.getRoles()){
                Hibernate.initialize(rol);
            }
        }
        return usuario;
    }

    @Override
    public List<Usuario> findAll() {
        List<Usuario> usuarios = em.createQuery("select object(o) from Usuario as o").getResultList();
        Iterator<Usuario> it = usuarios.iterator();
        while (it.hasNext()) {
            Usuario usuario = it.next();
            Hibernate.initialize(usuario);
            if (usuario.getRoles() != null) {
                Collection<Rol> roles = usuario.getRoles();
                for (Rol rol : roles) {
                    Hibernate.initialize(rol);
                }
            }
            if (usuario.getPersona() != null) {
                Hibernate.initialize(usuario.getPersona());
            }
        }
        return usuarios;
    }

    @Override
    public Usuario findByLogin(String login) throws NoResultException {
        try {
            Usuario usuario = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("correo", login).getSingleResult();
            Hibernate.initialize(usuario);
            if (usuario.getRoles() != null) {
                Collection<Rol> roles = usuario.getRoles();
                for (Rol rol1 : roles) {
                    Hibernate.initialize(rol1);
                }
            }
            if (usuario.getPersona() != null) {
                Hibernate.initialize(usuario.getPersona());
            }
            return usuario;
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            Usuario usuario = (Usuario) em.createNamedQuery("Usuario.findByLogin").setParameter("correo", login).getResultList().get(0);
            Hibernate.initialize(usuario);
            if (usuario.getRoles() != null) {
                Collection<Rol> roles = usuario.getRoles();
                for (Rol rol1 : roles) {
                    Hibernate.initialize(rol1);
                }
            }
            if (usuario.getPersona() != null) {
                Hibernate.initialize(usuario.getPersona());
            }
            return usuario;
        }
    }

    public List<Usuario> findByRol(String rol) {
        List<Usuario> usuarios = em.createNamedQuery("Usuario.findByRol").setParameter("rol", rol).getResultList();
        for (Usuario usuario : usuarios) {
            Hibernate.initialize(usuario);
            if (usuario.getRoles() != null) {
                Collection<Rol> roles = usuario.getRoles();
                for (Rol rol1 : roles) {
                    Hibernate.initialize(rol1);
                }
            }
            if (usuario.getPersona() != null) {
                Hibernate.initialize(usuario.getPersona());
            }
        }
        return usuarios;
    }
}
