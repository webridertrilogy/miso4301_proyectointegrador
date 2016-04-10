package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Servicios de negocio para administración de correos de auditoría (remoto)
 * @author Marcela Morales
 */
@Remote
public interface CorreoAuditoriaRemote {
    
    String consultarCorreosAuditoria(String comandoXML);
    
    String consultarCorreoAuditoria(String comandoXML);
    
    String eliminarCorreoAuditoria(String comandoXML);

    String consultarCorreoAuditoriaPorDestinatariosFechaYAsunto(String comandoXML);

    String eliminarCorreoAuditoriaPorDestinatariosFechaYAsunto(String comandoXML);

}
