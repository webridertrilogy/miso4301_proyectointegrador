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

import co.uniandes.sisinfo.entities.Regla;
import java.util.List;
import javax.ejb.Remote;

/**
 * Interface Remota
 * Servicios Entidad Regla
 */
@Remote
public interface ReglaFacadeRemote {

    /**
     * Crea la regla que llega como parámetro
     * @param regla Regla a crear
     */
    void create(Regla regla);

    /**
     * Edita la regla que llega como parámetro
     * @param regla Regla a editar
     */
    void edit(Regla regla);

    /**
     * Elimina la regla que llega como parámetro
     * @param regla Regla a eliminar
     */
    void remove(Regla regla);

    /**
     * Retorna la regla cuyo id llega como parámetro
     * @param id Id de la regla
     * @return regla cuyo id es el que llega como parámetro
     */
    Regla find(Object id);

    /**
     * Retorna una lista con todos las reglas
     * @return lista de reglas
     */
    List<Regla> findAll();

    /**
     * Retorna la regla cuyo id llega como parámetro
     * @param id Id de la regla
     * @return regla cuyo id es el que llega como parámetro
     */
    Regla findById(Long id);

    /**
     * Retorna la regla cuyo nombre llega como parámetro
     * @param nombre Nombre de la regla
     * @return regla cuyo nombre es el que llega como parámetro
     */
    Regla findByNombre(String nombre);
}
