/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Regla;
import co.uniandes.sisinfo.serviciosfuncionales.ReglaFacadeRemote;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 * Servicios Entidad Regla
 */
@Stateless
@EJB(name = "ReglaFacade", beanInterface = co.uniandes.sisinfo.serviciosfuncionales.ReglaFacadeLocal.class)
public class ReglaFacade implements ReglaFacadeLocal, ReglaFacadeRemote {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(Regla regla) {
        em.persist(regla);
    }

    @Override
    public void edit(Regla regla) {
        em.merge(regla);
    }

    @Override
    public void remove(Regla regla) {
        em.remove(em.merge(regla));
    }

    @Override
    public Regla find(Object id) {
        return em.find(Regla.class, id);
    }

    @Override
    public List<Regla> findAll() {
        return em.createQuery("select object(o) from Regla as o").getResultList();
    }

    @Override
    public Regla findById(Long id) {
        try {
            return (Regla) em.createNamedQuery("Regla.findById").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return  (Regla) em.createNamedQuery("Regla.findById").setParameter("id", id).getResultList().get(0);
        }
    }

    @Override
    public Regla findByNombre(String nombre) {
        try {
            return (Regla) em.createNamedQuery("Regla.findByNombre").setParameter("nombre", nombre).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (Regla) em.createNamedQuery("Regla.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
        }
    }
}
