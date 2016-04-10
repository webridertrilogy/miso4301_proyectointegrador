/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.Seccion;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author da-naran
 */
@Remote
public interface SeccionFacadeRemote {

    void create(Seccion seccion);

    void edit(Seccion seccion);

    void remove(Seccion seccion);

    Seccion find(Object id);

    List<Seccion> findAll();

    List<Seccion> findRange(int[] range);

    int count();

    void removeAll();

     /**
     * Retorna la sección cuyo crn llega como parámetro
     * @param crn Crn de la sección
     * @return sección cuyo crn es el que llega como parámetro
     */
    Seccion findByCRN(String crn);

    /**
     * Retorna si hay vacantes en la sección cuyo id llega como parámetro
     * @param idSeccion Id de la sección
     * @return true|false resultado de si hay vacantes en la sección
     */
    @Deprecated
    boolean hayVacantes(String idSeccion);

    /**
     * Retorna la sección cuyo id llega como parámetro
     * @param id Id de la sección
     * @return sección cuyo id es el que llega como parámetro
     */
    Seccion findById(Long id);

    /**
     * Retorna una lista con las secciones de un profesor dado su correo
     * @param correo Correo del profesor
     * @return Lista de secciones
     */
    List<Seccion> findByCorreoProfesor(String correo);

    @Deprecated
    long contarSeccionesConMonitoresCompletos();

    @Deprecated
    long contarSeccionesSinMonitores();

    long contarSecciones();

    @Deprecated
    long contarSeccionesRequierenMonitores();
}
