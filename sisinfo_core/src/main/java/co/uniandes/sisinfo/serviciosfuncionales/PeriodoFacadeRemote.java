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

import co.uniandes.sisinfo.entities.Periodo;
import java.util.List;
import javax.ejb.Remote;


/**
 * Interface Remota
 * Servicios Entidad Periodo
 */
@Remote
public interface PeriodoFacadeRemote {

    /**
     * Crea el periodo que llega como parámetro
     * @param periodo Periodo a crear
     */
    void create(Periodo periodo);

    /**
     * Edita el periodo que llega como parámetro
     * @param periodo Periodo a editar
     */
    void edit(Periodo periodo);

    /**
     * Elimina el periodo que llega como parámetro
     * @param periodo Periodo a eliminar
     */
    void remove(Periodo periodo);

    /**
     * Retorna el periodo cuyo id llega como parámetro
     * @param id Id del periodo
     * @return periodo cuyo id es el que llega como parámetro
     */
    Periodo find(Object id);

    /**
     * Retorna una lista con todos los periodos
     * @return lista de periodos
     */
    List<Periodo> findAll();

    /**
     * Retorna el periodo cuyo periodo llega como parámetro
     * @param periodo Periodo del periodo
     * @return periodo cuyo periodo es el que llega como parámetro
     */
    Periodo findByPeriodo(String periodo);

    /**
     * Retonra el periodo que esta marcado como actual
     * @return periodo El periodo marcado como actual
     */
    Periodo findActual();
}
