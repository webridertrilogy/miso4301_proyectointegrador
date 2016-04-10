/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.InscripcionEventoExterno;
import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Administrador
 */
@Remote
public interface InscripcionEventoExternoFacadeRemote {

    void create(InscripcionEventoExterno inscripcionEventoExterno);

    void edit(InscripcionEventoExterno inscripcionEventoExterno);

    void remove(InscripcionEventoExterno inscripcionEventoExterno);

    InscripcionEventoExterno find(Object id);

    List<InscripcionEventoExterno> findAll();

    Collection<InscripcionEventoExterno> findInscripcionesByIdContacto(Long id);

    List<InscripcionEventoExterno> findRange(int[] range);

    int count();
}
