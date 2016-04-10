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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Servicio Facade Documento Privado
 */
@Stateless
public class DocumentoPrivadoFacade implements DocumentoPrivadoFacadeLocal, DocumentoPrivadoFacadeRemote {

    @PersistenceContext
    private EntityManager em;

    public void create(DocumentoPrivado documentoPrivado) {
        em.persist(documentoPrivado);
    }

    public void edit(DocumentoPrivado documentoPrivado) {
        em.merge(documentoPrivado);
    }

    public void remove(DocumentoPrivado documentoPrivado) {
        em.remove(em.merge(documentoPrivado));
    }

    public DocumentoPrivado find(Object id) {
        return em.find(DocumentoPrivado.class, id);
    }

    public List<DocumentoPrivado> findAll() {
        return em.createQuery("select object(o) from DocumentoPrivado as o").getResultList();
    }
    
    public List<DocumentoPrivado> findActiveDocs() {
        return (List<DocumentoPrivado>) em.createNamedQuery("DocumentoPrivado.findByIsActive").setParameter("activo", true).getResultList();
    }
}
