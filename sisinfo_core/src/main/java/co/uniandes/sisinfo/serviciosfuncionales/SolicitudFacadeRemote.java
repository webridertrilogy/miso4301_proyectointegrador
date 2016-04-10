/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Solicitud;
import java.util.List;
import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicios Entidad Solicitud
 */
@Remote
public interface SolicitudFacadeRemote {

    /**
     * Crea la solicitud que llega como parámetro
     * @param solicitud Solicitud a crear
     */
    void create(Solicitud solicitud);

    /**
     * Edita la solicitud que llega como parámetro
     * @param solicitud Solicitud a editar
     */
    void edit(Solicitud solicitud);

    /**
     * Elimina la solicitud que llega como parámetro
     * @param solicitud Solicitud a eliminar
     */
    void remove(Solicitud solicitud);

    /**
     * Retorna la solicitud cuyo id llega como parámetro
     * @param id Id de la solicitud
     * @return solicitud cuyo id es el que llega como parámetro
     */
    Solicitud find(Object id);

    /**
     * Retorna una lista con todas las solicitudes
     * @return lista de solicitudes
     */
    List<Solicitud> findAll();

    /**
     * Retorna la solicitud cuyo id llega como parámetro
     * @param id Id de la solicitud
     * @return solicitud cuyo id es el que llega como parámetro
     */
    Solicitud findById(Long id);

    /**
     * Retorna una lista de solicitudes del estudiante cuyo código llega como parámetro
     * @param código Código del estudiante
     * @return lista de solicitudes del estudiante
     */
    List<Solicitud> findByCodigoEstudiante(String codigo);

    /**
     * Retorna una lista de solicitudes del estudiante cuyo login llega como parámetro
     * @param login Login del estudiante
     * @return lista de solicitudes del estudiante
     */
    List<Solicitud> findByLogin(String login);

    /**
     * Retorna una lista de solicitudes del estudiante cuyo login llega como parámetro
     * @param login Login del estudiante
     * @return lista de solicitudes del estudiante
     */
    List<Solicitud> tieneSolicitudesPorLogin(String login);

    /**
     * Retorna una lista de solicitudes de acuerdo a un estado dado
     * @param login Login del estudiante
     * @return lista de solicitudes del estudiante
     */
    List<Solicitud> findByEstado(String estado);
    /**
     * Retorna una lista de solicitudes de acuerdo a un CRN de sección dado
     * @param login Crn de la sección
     * @return lista de solicitudes
     */
    List<Solicitud> findByCrnSeccion(String crn);
    /**
     * Retorna una lista de solicitudes de acuerdo a un estado dado sin información de horario ni información académica
     * @param login Login del estudiante
     * @return lista de solicitudes del estudiante
     */
    List<Solicitud> findByEstadoParaSecretaria(String estado);

    /**
     * Retorna una lista de solicitudes de acuerdo a un estado y a un CRN de seccion
     * @param estado Estado de la solicitud
     * @param correo correo del profesor
     * @return lista de solicitudes pertinentes a la busqueda
     */
    List<Solicitud> findByEstadoAndProfesorPrincipalSeccion(String estado, String correo);

    /**
     * Retorna una lista de solicitudes de acuerdo a un estado y a un crn de seccion
     * @param estado Estado de la solicitud
     * @param crn crn de la seccion
     * @return lista de solicitudes pertinentes a la busqueda
     */
    List<Solicitud> findSolicitudesPreseleccionadasPorSeccion(String estado, String crn);

    /**
     * Retorna una lista de solicitudes del estudiante cuyo código llega como parámetro
     * y que tengan dentro de sus secciones la seccion parametrizada
     * @param código Código del estudiante
     * @param crn CRN de la seccion
     * @return lista de solicitudes del estudiante
     */
    List<Solicitud> findByCodigoEstudianteAndSeccion(String codigo, String crn);

    /**
     * Retorna una lista de solicitudes del estudiante cuyo código llega como parámetro
     * y que correspondan a un curso que entra por parametro
     * @param código Código del estudiante
     * @param codigoCurso codigo del curso
     * @return lista de solicitudes del estudiante
     */
    List<Solicitud> findByCodigoEstudianteAndCurso(String codigo, String codigoCurso);

    List<Solicitud> findByCorreoEstudianteAndCurso(String correo, String codigoCurso);

    List<Solicitud> findByCorreoEstudianteAndSeccion(String correo, String crn);

    List<Solicitud> findSolicitudesEnAspiracionPorSeccion(String estado, String crn);

    List<Solicitud> findByNotEstado(String estado);

    List<Solicitud> findByCurso(String codigoCurso);

    List<Solicitud> findByCursoCupi2AndEstado(String estado,String codigoCurso);

    List<Solicitud> findByCrnSeccionT2(String crnSeccion1, String crnSeccion2);

    List<Solicitud> findConveniosSecretaria();

    /**
     * Retorna una lista de solicitudes dado un conjunto de horas en un conjunto de dias
     * @param dias Arreglo con los nombres de los dias a revisar
     * @param horas Arregla con las horas dentro de los dias a revisar. El string
     * debe incluir todas las horas del dia (desde las 0:00 hasta las 24:00 considerando
     * franjas de 30 mins).
     * @return lista de solicitudes
     */
    List<Solicitud> findSolicitudesByDia(String dias, String horas);

    public List<Solicitud> findSolicitudesByHorario(String[] dias, String[] horas);

    public List<Solicitud> findSolicitudesByDiaYCurso(String dia, String hora,String codigo);

    public List<Solicitud> findSolicitudesByHorarioYCurso(String[] dias, String[] horas, String codigoCurso);

    public List<Solicitud> findSolicitudesResueltas();

}
