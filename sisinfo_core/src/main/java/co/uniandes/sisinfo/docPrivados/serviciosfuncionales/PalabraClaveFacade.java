/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package co.uniandes.sisinfo.docPrivados.serviciosfuncionales;

import co.uniandes.sisinfo.docPrivados.entities.PalabraClave;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicio Facade Entidad Palabra Clave
 */
@Stateless
public class PalabraClaveFacade implements PalabraClaveFacadeLocal, PalabraClaveFacadeRemote {

    @PersistenceContext
    private EntityManager em;

    public void create(PalabraClave palabraClave) {
        em.persist(palabraClave);
    }

    public void edit(PalabraClave palabraClave) {
        em.merge(palabraClave);
    }

    public void remove(PalabraClave palabraClave) {
        em.remove(em.merge(palabraClave));
    }

    public PalabraClave find(Object id) {
        return em.find(PalabraClave.class, id);
    }

    public List<PalabraClave> findAll() {
        return em.createQuery("select object(o) from PalabraClave as o").getResultList();
    }

    public PalabraClave findByPalabra(String palabra) {
        Query query = em.createNamedQuery("PalabraClave.findByPalabra").setParameter("palabra", palabra);
        try {            
            return (PalabraClave) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (PalabraClave) query.getResultList().get(0);
        }
    }
}
