package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.DiaDisponibilidad;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Servicios fachada de la entidad DatosContacto
 * @author Germán Florez, Marcela Morales
 */
@Stateless
public class DiaDisponibilidadFacade implements DiaDisponibilidadFacadeLocal, DiaDisponibilidadFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

   public void create(DiaDisponibilidad DiaDisponibilidad) {
        em.persist(DiaDisponibilidad);
    }

    public void edit(DiaDisponibilidad DiaDisponibilidad) {
        em.merge(DiaDisponibilidad);
    }

    public void remove(DiaDisponibilidad DiaDisponibilidad) {
        em.remove(em.merge(DiaDisponibilidad));
    }

    public DiaDisponibilidad find(Object id) {
        return em.find(DiaDisponibilidad.class, id);
    }

    public List<DiaDisponibilidad> findAll() {
        return em.createQuery("select object(o) from DiaDisponibilidad as o").getResultList();
    }

    public void removeAll(){
        List<DiaDisponibilidad> objs = findAll();
        for (DiaDisponibilidad t : objs) {
            remove(t);
        }
    }
}
