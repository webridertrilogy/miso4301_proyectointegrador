/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.docPrivados.serviciosfuncionales;

import co.uniandes.sisinfo.docPrivados.entities.Publicador;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicio Facade Entidad Publicador
 */
@Stateless
public class PublicadorFacade implements PublicadorFacadeLocal {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(Publicador publicador) {
        em.persist(publicador);
    }

    public void edit(Publicador publicador) {
        em.merge(publicador);
    }

    public void remove(Publicador publicador) {
        em.remove(em.merge(publicador));
    }

    public Publicador find(Object id) {
        return em.find(Publicador.class, id);
    }

    public List<Publicador> findAll() {
        return em.createQuery("select object(o) from Publicador as o").getResultList();
    }

    public Publicador findByCorreo(String correo) {
        Query query = em.createNamedQuery("Publicador.findByCorreo").setParameter("correo", correo);
        try {
            return (Publicador) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (Publicador) query.getResultList().get(0);
        }
    }
}
