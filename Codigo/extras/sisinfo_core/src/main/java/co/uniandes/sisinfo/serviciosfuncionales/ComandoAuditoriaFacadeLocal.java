/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ComandoAuditoria;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Paola Gómez
 */
@Local
public interface ComandoAuditoriaFacadeLocal {

    void create(ComandoAuditoria constante);

    void edit(ComandoAuditoria constante);

    void remove(ComandoAuditoria constante);

    ComandoAuditoria find(Object id);

    List<ComandoAuditoria> findAll();

    ComandoAuditoria findById(Long id);

    ComandoAuditoria findByNombre(String nombre);
}
