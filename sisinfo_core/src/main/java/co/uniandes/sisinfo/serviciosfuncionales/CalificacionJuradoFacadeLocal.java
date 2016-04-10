/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CalificacionJurado;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface CalificacionJuradoFacadeLocal {

    void create(CalificacionJurado calificacionJurado);

    void edit(CalificacionJurado calificacionJurado);

    void remove(CalificacionJurado calificacionJurado);

    CalificacionJurado find(Object id);

    List<CalificacionJurado> findAll();

    List<CalificacionJurado> findRange(int[] range);

    int count();

    CalificacionJurado findByHash(String hash);

}
