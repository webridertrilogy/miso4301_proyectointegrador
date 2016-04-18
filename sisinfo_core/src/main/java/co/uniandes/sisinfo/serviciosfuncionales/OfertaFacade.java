package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Oferta;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Servicios fachada de la entidad Oferta
 * @author Marcela Morales
 */
@Stateless
public class OfertaFacade implements OfertaFacadeLocal, OfertaFacadeRemote {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(Oferta oferta) {
        em.persist(oferta);
    }

    public void edit(Oferta oferta) {
        em.merge(oferta);
    }

    public void remove(Oferta oferta) {
        System.out.println("ID OFERTA A ELIMINAR:" + oferta.getId());
        em.remove(em.merge(oferta));
    }

    public Oferta find(Object id) {
        return em.find(Oferta.class, id);
    }

    public List<Oferta> findAll() {
        return em.createQuery("select object(o) from Oferta as o").getResultList();
    }

    public Oferta findyById(Long id) {
        try {
            return (Oferta) em.createNamedQuery("Oferta.findById").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (Oferta) em.createNamedQuery("Oferta.findById").setParameter("id", id).getResultList().get(0);
        }
    }

    public List<Oferta> findByEstado(String estado) {
        try {
            Query q = em.createNamedQuery("Oferta.findByEstado");

            q = q.setParameter("estado", estado);
            return  q.getResultList();

        } catch (NoResultException e) {
            return null;
        }
    }
}
