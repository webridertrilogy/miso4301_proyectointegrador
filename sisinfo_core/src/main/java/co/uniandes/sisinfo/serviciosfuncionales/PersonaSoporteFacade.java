package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.base.AbstractFacade;
import co.uniandes.sisinfo.entities.PersonaSoporte;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.Hibernate;

/**
 *
 * @author Juan Manuel Moreno B.
 */
@Stateless
public class PersonaSoporteFacade extends AbstractFacade<PersonaSoporte> implements PersonaSoporteFacadeLocal, PersonaSoporteFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public PersonaSoporteFacade() {
        super(PersonaSoporte.class);
    }

     private void hibernateInitialize(PersonaSoporte p) {
        Hibernate.initialize(p);
        Hibernate.initialize(p.getPersona());
        Hibernate.initialize(p.getIncidentes());
    }

    /**
     * Encuentra una persona de soporte por su correo.
     * @param id
     * @return
     */
    public PersonaSoporte findByEmail(String email) {

        try {

             PersonaSoporte p = (PersonaSoporte)em.createNamedQuery("PersonaSoporte.findByEmail").setParameter("correo", email).getSingleResult();
             hibernateInitialize(p);
             return p;
        } catch (NonUniqueResultException e) {

            List<PersonaSoporte> list = em.createNamedQuery("PersonaSoporte.findByEmail").setParameter("correo", email).getResultList();
            PersonaSoporte p =  list.get(0);
            hibernateInitialize(p);
            return p;
        } catch (NoResultException e) {

            return null;
        }
     }
}
