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

import co.uniandes.sisinfo.entities.Convocatoria;
import java.util.List;
import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicios Entidad Convocatoria
 */
@Remote
public interface ConvocatoriaFacadeRemote {

    /**
     * Crea la convocatoria que llega como parámetro
     * @param convocatoria Convocatoria a crear
     */
    void create(Convocatoria convocatoria);

    /**
     * Edita la convocatoria que llega como parámetro
     * @param convocatoria Convocatoria a editar
     */
    void edit(Convocatoria convocatoria);

    /**
     * Elimina la convocatoria que llega como parámetro
     * @param convocatoria Convocatoria a eliminar
     */
    void remove(Convocatoria convocatoria);

    /**
     * Retorna la convocatoria cuyo id llega como parámetro
     * @param id Id de la convocatoria
     * @return convocatoria cuyo id es el que llega como parámetro
     */
    Convocatoria find(Object id);

    /**
     * Retorna una lista con todas las convocatorias
     * @return lista de convocatorias
     */
    List<Convocatoria> findAll();
}
