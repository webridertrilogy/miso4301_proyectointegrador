/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.Sesion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author da-naran
 */
@Local
public interface SesionFacadeLocal {

    void create(Sesion sesion);

    void edit(Sesion sesion);

    void remove(Sesion sesion);

    Sesion find(Object id);

    List<Sesion> findAll();

    List<Sesion> findRange(int[] range);

    int count();

    void removeAll();

}
