package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.DatosContacto;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 * Servicios fachada de la entidad DatosContacto
 * @author Germán Florez, Marcela Morales
 */
@Stateless
public class DatosContactoFacade implements DatosContactoFacadeLocal, DatosContactoFacadeRemote {
    @PersistenceContext(unitName = "SoporteSisinfoPU")
    private EntityManager em;

    public void create(DatosContacto DatosContacto) {
        em.persist(DatosContacto);
    }

    public void edit(DatosContacto DatosContacto) {
        em.merge(DatosContacto);
    }

    public void remove(DatosContacto DatosContacto) {
        em.remove(em.merge(DatosContacto));
    }

    public DatosContacto find(Object id) {
        return em.find(DatosContacto.class, id);
    }

    public List<DatosContacto> findAll() {
        return em.createQuery("select object(o) from DatosContacto as o").getResultList();
    }

    public DatosContacto buscarContactoPorNombre(String nombre) {
        try {
            return (DatosContacto) em.createNamedQuery("DatosContacto.findByNombre").setParameter("nombre", nombre).getSingleResult();
        } catch(NonUniqueResultException nure){
            return (DatosContacto)em.createNamedQuery("DatosContacto.findByNombre").setParameter("nombre", nombre).getResultList().get(0);
        } catch(NoResultException nre){
            return null;
        }
    }
}
