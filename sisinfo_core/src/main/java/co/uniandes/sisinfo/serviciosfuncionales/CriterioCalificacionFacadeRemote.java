/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.CriterioCalificacion;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Remote
public interface CriterioCalificacionFacadeRemote {

    void create(CriterioCalificacion criterioCalificacion);

    void edit(CriterioCalificacion criterioCalificacion);

    void remove(CriterioCalificacion criterioCalificacion);

    CriterioCalificacion find(Object id);

    List<CriterioCalificacion> findAll();

    List<CriterioCalificacion> findRange(int[] range);

    int count();

}
