/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosnegocio;

import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicio de negocio: Administración de solicitudes
 */
@Remote
public interface SolicitudRemote {

    /**
     * Metodo que se encarga de crear una solicitud dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String crearsolicitud(String pathXML);

    /**
     * Metodo que se encarga de dar la informacion de una solicitud dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darSolicitudPorId(String xml);

    /**
     * Metodo que se encarga de retornar las solicitudes de un estudiante dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darSolicitudesEstudiantePorCodigo(String xml);

    /**
     * Metodo que se encarga de retornar las solicitudes de un estudiante dado su login dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darSolicitudesEstudiantePorLogin(String xml);

    /**
     * Metodo que se encarga de modificar una solicitud dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    //String modificarSolicitud(String xml);
    /**
     * Metodo que se encarga de elimianr una solicitud dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String eliminarSolicitud(String xml);
    /**
     * Método que se encarga de eliminar una solicitud dado su id
     * @param idSolicitud identificador en base de datos de la solicitud
     * @param rolResponsable rolResponsable de eliminar la solicitud
     * @param mensaje. Mensaje que se le envia al estudiante
     */
    public void eliminarSolicitud(Long idSolicitud,String rolResponsable,String mensaje);

    /**
     * Metodo que se encarga de consultar las solicitudes con un estado dado el comando respectivo
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darSolicitudesPorEstado(String xml);


    /**
     * Metodo que se encarga de consultar las solicitudes que han sido
     * preseleccionadas para una seccion determinada
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darSolicitudesPreseleccionadasPorSeccion(String xml);

    /**
     * Metodo que se encarga de consultar las solicitudes en estado aspirante
     * para una seccion determinada
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darSolicitudesEnAspiracionPorSeccion(String xml);

    /**
     * Metodo que se encarga de cancelar una solicitud por carga academica dado un id
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String cancelarSolicitudPorCarga(String xml);

    /**
     * Metodo que se encarga de consultar todas las solicitudes
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darSolicitudes(String comando);

    /**
     * Metodo que se encarga de consultar todas las solicitudes de monitores candidatos T2
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String darSolicitudesMonitoresCandidatosT2(String comando);


    /**
     * Método que se encarga de actualizar la información de solicitud del estudiante
     * @param xml Cadena XML en la cual se define el comando
     * @return Cadena XML con la respuesta del comando
     */
    String actualizarSolicitud(String xml);

    String darSolicitudesSinMonitoria(String comando);

    String darSeccionesAPOIPorCorreo(String comando);

    String tieneSolicitudesPorLogin(String xml);

    String consultarMonitoresT2(String comando);

    String consultarSeccionesSinMonitorT2(String comando);

    void removerHorarioSeccionAAspirante(String crn,String correo);

    void agregarHorarioSeccionAAspirante(String crn,String correo);

    String consultarSolicitudes(String comando);

    String consultarSolicitudesResueltas(String xml);
}
