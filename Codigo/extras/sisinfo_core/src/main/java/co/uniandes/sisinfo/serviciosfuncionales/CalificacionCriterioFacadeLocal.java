/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CalificacionCriterio;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface CalificacionCriterioFacadeLocal {

    void create(CalificacionCriterio calificacionCriterio);

    void edit(CalificacionCriterio calificacionCriterio);

    void remove(CalificacionCriterio calificacionCriterio);

    CalificacionCriterio find(Object id);

    List<CalificacionCriterio> findAll();

    List<CalificacionCriterio> findRange(int[] range);

    int count();

}
