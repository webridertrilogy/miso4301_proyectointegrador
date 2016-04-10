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

import co.uniandes.sisinfo.entities.MonitoriaAceptada;
import java.util.List;
import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicios Entidad Monitoría
 */
@Remote
public interface MonitoriaFacadeRemote {

    /**
     * Crea la monitoría que llega como parámetro
     * @param MonitoriaAceptada Monitoría a crear
     */
    void create(MonitoriaAceptada MonitoriaAceptada);

    /**
     * Edita la monitoría que llega como parámetro
     * @param MonitoriaAceptada Monitoría a editar
     */
    void edit(MonitoriaAceptada MonitoriaAceptada);

    /**
     * Elimina la monitoría que llega como parámetro
     * @param MonitoriaAceptada Monitoría a eliminar
     */
    void remove(MonitoriaAceptada MonitoriaAceptada);

    /**
     * Retorna la monitoría cuyo id llega como parámetro
     * @param id Id de la monitoría
     * @return monitoría cuyo id es el que llega como parámetro
     */
    MonitoriaAceptada find(Object id);

    /**
     * Retorna una lista con todas las monitorías
     * @return lista de monitorías
     */
    List<MonitoriaAceptada> findAll();

    List<MonitoriaAceptada> findByCodigoEstudiante(String codigo);

    List<MonitoriaAceptada> findByCRNSeccion(String crn);

    MonitoriaAceptada findByCRNYCorreo(String crn, String correo);

    MonitoriaAceptada findBySolicitud(long id);
}
