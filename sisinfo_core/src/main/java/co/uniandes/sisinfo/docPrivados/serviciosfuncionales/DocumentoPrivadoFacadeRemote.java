/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 *
 * Autores: Marcela Morales, David Naranjo, Juan Camilo Cortés
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package co.uniandes.sisinfo.docPrivados.serviciosfuncionales;

import co.uniandes.sisinfo.docPrivados.entities.DocumentoPrivado;
import java.util.List;
import javax.ejb.Remote;

/**
 * Interface Remota Facade Documento Privado
 */
@Remote
public interface DocumentoPrivadoFacadeRemote {

    void create(DocumentoPrivado documentoPrivado);

    void edit(DocumentoPrivado documentoPrivado);

    void remove(DocumentoPrivado documentoPrivado);

    DocumentoPrivado find(Object id);

    List<DocumentoPrivado> findAll();

    List<DocumentoPrivado> findActiveDocs();
}
