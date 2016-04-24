package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.HojaVida;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Servicios fachada de la entidad HojaVida
 * @author Marcela Morales
 */
@Stateless
public class HojaVidaFacade implements HojaVidaFacadeLocal {
	@PersistenceContext(unitName="SoporteSisinfoPU")
    private EntityManager em;

    public void create(HojaVida hojaVida) {
        em.persist(hojaVida);
    }

    public void edit(HojaVida hojaVida) {
        em.merge(hojaVida);
    }

    public void remove(HojaVida hojaVida) {
        em.remove(em.merge(hojaVida));
    }

    public HojaVida find(Object id) {
        return em.find(HojaVida.class, id);
    }

    public List<HojaVida> findAll() {
        return em.createQuery("select object(o) from HojaVida as o").getResultList();
    }
}
