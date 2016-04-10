package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicios de consulta y edición de datos de usuarios
 * @author Marcela Morales
 */
@Remote
public interface ConsultaRolBeanRemote {

    String consultarConstantesRoles(String xml);

    String consultarUsuarios(String xml);

    String guardarRolUsuarios(String xml);

    String crearUsuario(String xml);

    String consultarConstantesPais(String xml);

    String consultarConstantesTipoDocumento(String xml);

    String consultarUsuario(String comandoXML);
}
