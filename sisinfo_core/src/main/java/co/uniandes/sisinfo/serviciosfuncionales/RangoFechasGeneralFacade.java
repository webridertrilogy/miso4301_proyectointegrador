package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.RangoFechasGeneral;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 * Servicios fachada de la entidad RangoFechasGeneral
 * @author Marcela Morales
 */
@Stateless
public class RangoFechasGeneralFacade implements RangoFechasGeneralFacadeLocal, RangoFechasGeneralFacadeRemote {

	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    @Override
    public void create(RangoFechasGeneral rangoFechasGeneral) {
        em.persist(rangoFechasGeneral);
    }

    @Override
    public void edit(RangoFechasGeneral rangoFechasGeneral) {
        em.merge(rangoFechasGeneral);
    }

    @Override
    public void remove(RangoFechasGeneral rangoFechasGeneral) {
        em.remove(em.merge(rangoFechasGeneral));
    }

    @Override
    public void removeAll(){
        for(RangoFechasGeneral rangoFechasGeneral : findAll()){
            remove(rangoFechasGeneral);
        }
    }

    @Override
    public RangoFechasGeneral find(Object id) {
        return em.find(RangoFechasGeneral.class, id);
    }

    @Override
    public List<RangoFechasGeneral> findAll() {
        return em.createQuery("select object(o) from RangoFechasGeneral as o").getResultList();
    }

    @Override
    public RangoFechasGeneral findByNombre(String nombre) {
        try {
            return (RangoFechasGeneral) em.createNamedQuery("RangoFechasGeneral.findByNombre").setParameter("nombre", nombre).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException e) {
            return (RangoFechasGeneral) em.createNamedQuery("RangoFechasGeneral.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
        }
    }

    public RangoFechasGeneral findById(Long id) {
        try {
            return (RangoFechasGeneral) em.createNamedQuery("RangoFechasGeneral.findById").setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return (RangoFechasGeneral) em.createNamedQuery("RangoFechasGeneral.findById").setParameter("id", id).getResultList().get(0);

        }
    }
}
