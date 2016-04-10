/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package co.uniandes.sisinfo.docPrivados.serviciosfuncionales;

import co.uniandes.sisinfo.docPrivados.entities.Publicador;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface Local Entidad Publicador
 */
@Local
public interface PublicadorFacadeLocal {

    void create(Publicador publicador);

    void edit(Publicador publicador);

    void remove(Publicador publicador);

    Publicador find(Object id);

    List<Publicador> findAll();

    Publicador findByCorreo(String correo);
}
