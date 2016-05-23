package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CorreoAuditoria;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 * Servicios fachada entidad CorreoAuditoría (Local)
 * @author Manuel Rodríguez, Marcela Morales
 */
@Local
public interface CorreoAuditoriaFacadeLocal {

    void create(CorreoAuditoria correoAuditoria);

    void edit(CorreoAuditoria correoAuditoria);

    void remove(CorreoAuditoria correoAuditoria);

    CorreoAuditoria find(Object id);

    List<CorreoAuditoria> findAll();

    List<CorreoAuditoria> findRange(int[] range);

    int count();

    List<CorreoAuditoria> findByDestinatarios(String correo);

    List<CorreoAuditoria> findByFecha(Date fechaInicio, Date fechaFin);
    
    List<CorreoAuditoria> findByAsunto(String asunto);

    List<CorreoAuditoria> findByDestinatariosYFecha(String correo, Date fechaInicio, Date fechaFin);

    List<CorreoAuditoria> findByAsuntoYFecha(String asunto, Date fechaInicio, Date fechaFin);

    List<CorreoAuditoria> findByDestinatariosYAsunto(String correo, String asunto);

    List<CorreoAuditoria> findByDestinatariosFechaYAsunto(String correo, Date fechaInicio, Date fechaFin, String asunto);

}
