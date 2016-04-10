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

import co.uniandes.sisinfo.entities.Aspirante;
import java.util.List;
import javax.ejb.Remote;


/**
 * Interface Remota
 * Servicios Entidad Aspirante
 */
@Remote
public interface AspiranteFacadeRemote {

    /**
     * Crea el aspirante que llega como parámetro
     * @param aspirante Aspirante a crear
     */
    void create(Aspirante aspirante);

    /**
     * Edita el aspirante que llega como parámetro
     * @param aspirante Aspirante a editar
     */
    void edit(Aspirante aspirante);

    /**
     * Elimina el aspirante que llega como parámetro
     * @param aspirante Aspirante a eliminar
     */
    void remove(Aspirante aspirante);

    /**
     * Retorna el aspirante cuyo id llega como parámetro
     * @param id Id del aspirante
     * @return aspirante cuyo id es el que llega como parámetro
     */
    Aspirante find(Object id);

    /**
     * Retorna una lista con todos los aspirantes
     * @return lista de aspirantes
     */
    List<Aspirante> findAll();

    /**
     * Retorna el aspirante cuyo código llega como parámetro
     * @param codigo Código del aspirante
     * @return aspirante cuyo código es el que llega como parámetro
     */
    Aspirante findByCodigo(String codigo);

    /**
     * Retorna el aspirante cuyo correo llega como parámetro
     * @param correo Correo del aspirante
     * @return aspirante cuyo correo es el que llega como parámetro
     */
    Aspirante findByCorreo(String correo);
}
