/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.Persona;
import java.util.List;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.Hibernate;

/**
 *
 * @author da-naran
 */
@Stateless
public class PersonaFacade implements PersonaFacadeLocal, PersonaFacadeRemote {

    @PersistenceContext
    private EntityManager em;

    public void create(Persona persona) {
        em.persist(persona);

    }

    public void edit(Persona persona) {
        em.merge(persona);
    }

    public void refresh(Persona persona) {
        em.refresh(persona);
    }

    public void remove(Persona persona) {
        em.remove(em.merge(persona));
    }

    public Persona find(Object id) {
        return em.find(Persona.class, id);
    }

    public List<Persona> findAll() {
        return em.createQuery("select object(o) from Persona as o").getResultList();
    }

    public Persona findByCorreo(String correo) {
        try {
            Persona persona = (Persona) em.createNamedQuery("Persona.findByCorreo").setParameter("correo", correo).getSingleResult();
            Hibernate.initialize(persona);
            if(persona.getTipoDocumento() != null){
                Hibernate.initialize(persona.getTipoDocumento());
                Hibernate.initialize(persona.getTipoDocumento().getTipo());
            }
            if(persona.getPais() != null){
                Hibernate.initialize(persona.getPais());
                Hibernate.initialize(persona.getPais().getNombre());
            }
            return persona;
        } catch (NonUniqueResultException nure) {
            return (Persona) em.createNamedQuery("Persona.findByCorreo").setParameter("correo", correo).getResultList().get(0);
        } catch (NoResultException nre) {
            return null;
        }
    }

    public Persona findLikeCorreo(String correo) {
        try {
            return (Persona) em.createNamedQuery("Persona.findLikeCorreo").setParameter("correo", "%" + correo + "%").getSingleResult();
        } catch (NonUniqueResultException nure) {
            return (Persona) em.createNamedQuery("Persona.findLikeCorreo").setParameter("correo", "%" + correo + "%").getResultList().get(0);
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void flush() {
        em.flush();
    }

    @Deprecated
    public void crearPersonaPorLogin(String login) throws NamingException {
        Persona p = new Persona();
////        String nombres = AccesoLDAP.obtenerNombres(login);
////        String apellidos = AccesoLDAP.obtenerApellidos(login);
////        String codigo = AccesoLDAP.obtenerCodigo(login);
////        String correo = AccesoLDAP.obtenerCorreo(login);
//        p.setCorreo(correo);
//        p.setNombres(nombres);
//        p.setApellidos(apellidos);
//        p.setCodigo(codigo);
//        p.setActivo(true);
//        create(p);

    }
}
