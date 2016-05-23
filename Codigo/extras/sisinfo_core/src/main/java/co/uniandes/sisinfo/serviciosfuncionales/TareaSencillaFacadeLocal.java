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

import co.uniandes.sisinfo.entities.TareaSencilla;
import java.util.Collection;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface Local
 * Servicios Entidad Tarea
 */
@Local
public interface TareaSencillaFacadeLocal {

    void create(TareaSencilla tarea);

    void edit(TareaSencilla tarea);

    void remove(TareaSencilla tarea);

    TareaSencilla find(Object id);

    List<TareaSencilla> findAll();

    TareaSencilla findById(Long id);

    Collection<TareaSencilla> findByTipo(String tipo);

}
