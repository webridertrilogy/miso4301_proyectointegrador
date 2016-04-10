package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CorreoAuditoria;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Servicios fachada entidad CorreoAuditoría
 * @author Manuel Rodríguez, Marcela Morales
 */
@Stateless
public class CorreoAuditoriaFacade extends AbstractFacade<CorreoAuditoria> implements CorreoAuditoriaFacadeLocal, CorreoAuditoriaFacadeRemote {

    @PersistenceContext(unitName = "ServiciosInfraestructuraPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public CorreoAuditoriaFacade() {
        super(CorreoAuditoria.class);
    }

    public List<CorreoAuditoria> findByDestinatarios(String correo) {
        return em.createNamedQuery("CorreoAuditoria.findByDestinatarios").setParameter("correo", correo).getResultList();
    }

    public List<CorreoAuditoria> findByFecha(Date fechaInicio, Date fechaFin) {
        return em.createNamedQuery("CorreoAuditoria.findByFecha").setParameter("fechaInicio", fechaInicio).setParameter("fechaFin", fechaFin).getResultList();
    }

    public List<CorreoAuditoria> findByAsunto(String asunto) {
        return em.createNamedQuery("CorreoAuditoria.findByAsunto").setParameter("asunto", "%"+asunto+"%").getResultList();
    }

    public List<CorreoAuditoria> findByDestinatariosYFecha(String correo, Date fechaInicio, Date fechaFin) {
        return em.createNamedQuery("CorreoAuditoria.findByDestinatariosYFecha").setParameter("correo", correo).setParameter("fechaInicio", fechaInicio).setParameter("fechaFin", fechaFin).getResultList();
    }

    public List<CorreoAuditoria> findByAsuntoYFecha(String asunto, Date fechaInicio, Date fechaFin) {
        return em.createNamedQuery("CorreoAuditoria.findByAsuntoYFecha").setParameter("asunto", "%"+asunto+"%").setParameter("fechaInicio", fechaInicio).setParameter("fechaFin", fechaFin).getResultList();
    }

    public List<CorreoAuditoria> findByDestinatariosYAsunto(String correo, String asunto) {
        return em.createNamedQuery("CorreoAuditoria.findByDestinatariosYAsunto").setParameter("correo", correo).setParameter("asunto", asunto).getResultList();
    }

    public List<CorreoAuditoria> findByDestinatariosFechaYAsunto(String correo, Date fechaInicio, Date fechaFin, String asunto) {
        return em.createNamedQuery("CorreoAuditoria.findByDestinatariosFechaYAsunto").setParameter("correo", correo).setParameter("asunto", "%"+asunto+"%").setParameter("fechaInicio", fechaInicio).setParameter("fechaFin", fechaFin).getResultList();
    }
}
