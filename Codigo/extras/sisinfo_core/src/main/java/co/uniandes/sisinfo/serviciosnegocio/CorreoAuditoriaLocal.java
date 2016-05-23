package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 * Servicios de negocio para administración de correos de auditoría (local)
 * @author Marcela Morales
 */
@Local
public interface CorreoAuditoriaLocal {
    
    String consultarCorreosAuditoria(String comandoXML);
    
    String consultarCorreoAuditoria(String comandoXML);
    
    String eliminarCorreoAuditoria(String comandoXML);

    String consultarCorreoAuditoriaPorDestinatariosFechaYAsunto(String comandoXML);
    
    String eliminarCorreoAuditoriaPorDestinatariosFechaYAsunto(String comandoXML);

}