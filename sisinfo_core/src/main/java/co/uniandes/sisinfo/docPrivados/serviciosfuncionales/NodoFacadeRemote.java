/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package co.uniandes.sisinfo.docPrivados.serviciosfuncionales;

import co.uniandes.sisinfo.docPrivados.entities.Nodo;
import java.util.List;
import javax.ejb.Remote;

/**
 * Interface Remota Entidad Nodo
 */
@Remote
public interface NodoFacadeRemote {

    void create(Nodo nodo);

    void edit(Nodo nodo);

    void remove(Nodo nodo);

    Nodo find(Object id);

    List<Nodo> findAll();

    Nodo findByParentId(String parentId);

    Nodo findByDocumentoId(Long docId);
}
