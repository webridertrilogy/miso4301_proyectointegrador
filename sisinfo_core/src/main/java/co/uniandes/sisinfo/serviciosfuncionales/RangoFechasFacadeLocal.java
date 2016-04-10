/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.RangoFechas;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Administrador
 */
@Local
public interface RangoFechasFacadeLocal {

    void create(RangoFechas rangoFechas);

    void edit(RangoFechas rangoFechas);

    void remove(RangoFechas rangoFechas);

    RangoFechas find(Object id);

    List<RangoFechas> findAll();

    List<RangoFechas> findRange(int[] range);

    int count();

    RangoFechas findByNombre(String nombre);

}
