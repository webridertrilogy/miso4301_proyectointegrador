/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.AccionVencida;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface AccionVencidaFacadeLocal {

    void create(AccionVencida accionVencida);

    void edit(AccionVencida accionVencida);

    void remove(AccionVencida accionVencida);

    AccionVencida find(Object id);

    List<AccionVencida> findAll();
}
