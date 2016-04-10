/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.InscripcionAsistente;
import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author im.melo33
 */
@Remote
public interface InscripcionAsistenteFacadeRemote {

    void create(InscripcionAsistente inscripcion);
    void edit(InscripcionAsistente inscripcion);
    void remove(InscripcionAsistente inscripcion);
    void refresh(InscripcionAsistente inscrAsis);
    InscripcionAsistente find(Object id);
    List<InscripcionAsistente> findAll();
    InscripcionAsistente findByHash(String hashConfirm);

    Collection<InscripcionAsistente> darInscripcionesAsistentePorCorreo(String correo);

    InscripcionAsistente findByIdInscripcionYCorreoPersona(String correo, Long idInscripcion);
}
