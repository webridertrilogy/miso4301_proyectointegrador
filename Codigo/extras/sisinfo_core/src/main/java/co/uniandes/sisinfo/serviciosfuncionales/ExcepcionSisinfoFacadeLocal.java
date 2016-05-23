/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.ExcepcionSisinfo;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface ExcepcionSisinfoFacadeLocal {

    void create(ExcepcionSisinfo excepcionSisinfo);

    void edit(ExcepcionSisinfo excepcionSisinfo);

    void remove(ExcepcionSisinfo excepcionSisinfo);

    ExcepcionSisinfo find(Object id);

    List<ExcepcionSisinfo> findAll();

    List<ExcepcionSisinfo> findRange(int[] range);

    int count();

    List<ExcepcionSisinfo> findByComandoFechaMetodo(String comando, String nombreMetodo, Timestamp fecha);

    List<ExcepcionSisinfo> findByEstado(Boolean estado);

    List<ExcepcionSisinfo> findByNoBorradas();

   
}
