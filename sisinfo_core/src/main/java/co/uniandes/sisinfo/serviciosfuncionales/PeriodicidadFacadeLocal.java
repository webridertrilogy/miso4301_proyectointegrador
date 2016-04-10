/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.Periodicidad;
import co.uniandes.sisinfo.entities.*;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author david
 */
@Local
public interface PeriodicidadFacadeLocal {

    void create(Periodicidad periodicidad);

    void edit(Periodicidad periodicidad);

    void remove(Periodicidad periodicidad);

    Periodicidad find(Object id);

    List<Periodicidad> findAll();

    Periodicidad findByNombre(String nombre);

}
