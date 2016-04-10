/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ComentarioTesis;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo S
 */
@Local
public interface ComentarioTesisFacadeLocal {

    void create(ComentarioTesis comentarioTesis);

    void edit(ComentarioTesis comentarioTesis);

    void remove(ComentarioTesis comentarioTesis);

    ComentarioTesis find(Object id);

    List<ComentarioTesis> findAll();

    List<ComentarioTesis> findRange(int[] range);

    int count();

}
