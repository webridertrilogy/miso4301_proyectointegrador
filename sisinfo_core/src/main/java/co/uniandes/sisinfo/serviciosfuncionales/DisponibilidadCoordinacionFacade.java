package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.DisponibilidadCoordinacion;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Servicios fachada de la entidad DisponibilidadCoordinacion
 * @author Germán Florez, Marcela Morales
 */
@Stateless
public class DisponibilidadCoordinacionFacade implements DisponibilidadCoordinacionFacadeLocal, DisponibilidadCoordinacionFacadeRemote {
    @PersistenceContext(unitName = "ReservaCitasPU")
    private EntityManager em;

    public void create(DisponibilidadCoordinacion DisponibilidadCoordinacion) {
        em.persist(DisponibilidadCoordinacion);
    }

    public void edit(DisponibilidadCoordinacion DisponibilidadCoordinacion) {
        em.flush();
        em.merge(DisponibilidadCoordinacion);
    }

    public void remove(DisponibilidadCoordinacion DisponibilidadCoordinacion) {
        em.remove(em.merge(DisponibilidadCoordinacion));
    }

    public DisponibilidadCoordinacion find(Object id) {
        return em.find(DisponibilidadCoordinacion.class, id);
    }

    public List<DisponibilidadCoordinacion> findAll() {
        return em.createQuery("select object(o) from DisponibilidadCoordinacion as o").getResultList();
    }

    public void removeAll(){
        List<DisponibilidadCoordinacion> objs = findAll();
        for (DisponibilidadCoordinacion t : objs) {
            remove(t);
        }
    }
}
