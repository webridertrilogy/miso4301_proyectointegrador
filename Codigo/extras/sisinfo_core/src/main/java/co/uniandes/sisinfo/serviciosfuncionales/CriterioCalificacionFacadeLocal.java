/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CriterioCalificacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface CriterioCalificacionFacadeLocal {

    void create(CriterioCalificacion criterioCalificacion);

    void edit(CriterioCalificacion criterioCalificacion);

    void remove(CriterioCalificacion criterioCalificacion);

    CriterioCalificacion find(Object id);

    List<CriterioCalificacion> findAll();

    List<CriterioCalificacion> findRange(int[] range);

    int count();

}
