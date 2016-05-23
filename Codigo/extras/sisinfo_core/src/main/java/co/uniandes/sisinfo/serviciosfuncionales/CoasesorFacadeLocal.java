/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Coasesor;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Ivan Mauricio Melo Suarez
 */
@Local
public interface CoasesorFacadeLocal {

    void create(Coasesor coasesor);

    void edit(Coasesor coasesor);

    void remove(Coasesor coasesor);

    Coasesor find(Object id);

    List<Coasesor> findAll();

    List<Coasesor> findRange(int[] range);

    int count();

    Coasesor findByCorreo(String correo);

}
