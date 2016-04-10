/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CalificacionJurado;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Remote
public interface CalificacionJuradoFacadeRemote {

    void create(CalificacionJurado calificacionJurado);

    void edit(CalificacionJurado calificacionJurado);

    void remove(CalificacionJurado calificacionJurado);

    CalificacionJurado find(Object id);

    List<CalificacionJurado> findAll();

    List<CalificacionJurado> findRange(int[] range);

    int count();

    CalificacionJurado findByHash(String hash);

}
