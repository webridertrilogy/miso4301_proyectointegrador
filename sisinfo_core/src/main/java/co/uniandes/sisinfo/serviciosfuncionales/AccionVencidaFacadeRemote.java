/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.AccionVencida;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Remote
public interface AccionVencidaFacadeRemote {

    void create(AccionVencida accionVencida);

    void edit(AccionVencida accionVencida);

    void remove(AccionVencida accionVencida);

    AccionVencida find(Object id);

    List<AccionVencida> findAll();
}
