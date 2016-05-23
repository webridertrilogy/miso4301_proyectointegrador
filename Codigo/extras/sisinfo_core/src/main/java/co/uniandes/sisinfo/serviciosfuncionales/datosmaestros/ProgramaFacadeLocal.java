/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, Carlos Morales, Javier Bautista,
 *          Alejandro Osorio, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.Programa;
import co.uniandes.sisinfo.entities.datosmaestros.Programa;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Entidad Programa
 */
@Local
public interface ProgramaFacadeLocal {

    /**
     * Crea el programa que llega como parámetro
     * @param programa Programa a crear
     */
    void create(Programa programa);

    /**
     * Edita el programa que llega como parámetro
     * @param programa Programa a editar
     */
    void edit(Programa programa);

    /**
     * Eliminar el programa que llega como parámetro
     * @param programa Programa a eliminar
     */
    void remove(Programa programa);

    /**
     * Retorna el programa cuyo id llega como parámetro
     * @param id Id del programa
     * @return programa cuyo id es el que llega como parámetro
     */
    Programa find(Object id);

    /**
     * Retorna una lista con todos los programas
     * @return lista de programas
     */
    List<Programa> findAll();

    /**
     * Retorna el aspirante cuyo nombre llega como parámetro
     * @param nombre Nombre del aspirante
     * @return aspirante cuyo nombre es el que llega como parámetro
     */
    Programa findByNombre(String nombre);

    Programa findByCodigo(String codigo);

    void removeAll();
}
