package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.InformacionEmpresa;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Servicios fachada de la entidad Información Empresa
 * @author Marcela Morales
 */
@Stateless
public class InformacionEmpresaFacade implements InformacionEmpresaFacadeLocal, InformacionEmpresaFacadeRemote {
    @PersistenceContext
    private EntityManager em;

    public void create(InformacionEmpresa informacionEmpresa) {
        em.persist(informacionEmpresa);
    }

    public void edit(InformacionEmpresa informacionEmpresa) {
        em.merge(informacionEmpresa);
    }

    public void remove(InformacionEmpresa informacionEmpresa) {
        em.remove(em.merge(informacionEmpresa));
    }

    public InformacionEmpresa find(Object id) {
        return em.find(InformacionEmpresa.class, id);
    }

    public List<InformacionEmpresa> findAll() {
        return em.createQuery("select object(o) from InformacionEmpresa as o").getResultList();
    }
}
