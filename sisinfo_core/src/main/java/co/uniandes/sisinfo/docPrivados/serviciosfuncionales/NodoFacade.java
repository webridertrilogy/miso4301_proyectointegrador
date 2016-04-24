/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package co.uniandes.sisinfo.docPrivados.serviciosfuncionales;

import co.uniandes.sisinfo.docPrivados.entities.Nodo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicio Facade Entidad Nodo
 */
@Stateless
public class NodoFacade implements NodoFacadeLocal {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(Nodo nodo) {
        em.persist(nodo);
    }

    public void edit(Nodo nodo) {
        em.merge(nodo);
    }

    public void remove(Nodo nodo) {
        em.remove(em.merge(nodo));
    }

    public Nodo find(Object id) {
        return em.find(Nodo.class, id);
    }

    public List<Nodo> findAll() {
        return em.createQuery("select object(o) from Nodo as o").getResultList();
    }

    public Nodo findByParentId(String parentId) {
        Long idPadre = Long.parseLong(parentId);
        Query query = em.createNamedQuery("Nodo.findByPadreId").setParameter("padreId", idPadre);
        try {
            return (Nodo) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (Nodo) query.getResultList().get(0);
        }
    }

    public Nodo findByDocumentoId(Long docId) {
        Query query = em.createNamedQuery("Nodo.findByDocumentoId").setParameter("docId", docId);
        try {
            return (Nodo) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (Nodo) query.getResultList().get(0);
        }
    }
}
