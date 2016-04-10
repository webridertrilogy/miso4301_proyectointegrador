/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package co.uniandes.sisinfo.docPrivados.serviciosfuncionales;

import co.uniandes.sisinfo.docPrivados.entities.PalabraClave;
import java.util.List;
import javax.ejb.Remote;

/**
 * Interface Remota Entidad Palabra Clave
 */
@Remote
public interface PalabraClaveFacadeRemote {

    void create(PalabraClave palabraClave);

    void edit(PalabraClave palabraClave);

    void remove(PalabraClave palabraClave);

    PalabraClave find(Object id);

    List<PalabraClave> findAll();

    PalabraClave findByPalabra(String palabra);
}
