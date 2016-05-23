package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Local;

/**
 * Interface Local
 * Servicios de consulta y edición de datos de usuarios
 * @author Marcela Morales
 */
@Local
public interface ConsultaRolBeanLocal {

    String consultarConstantesRoles(String xml);

    String consultarUsuarios(String xml);

    String guardarRolUsuarios(String xml);

    String crearUsuario(String xml);

    String consultarConstantesPais(String xml);

    String consultarConstantesTipoDocumento(String xml);

    String consultarUsuario(String comandoXML);
}
