/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.NivelFormacion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author david
 */
@Local
public interface NivelFormacionFacadeLocal {

    void create(NivelFormacion nivelFormacion);

    void edit(NivelFormacion nivelFormacion);

    void remove(NivelFormacion nivelFormacion);

    NivelFormacion find(Object id);

    List<NivelFormacion> findAll();

    List<NivelFormacion> findRange(int[] range);

    int count();

    NivelFormacion findByName(String name);

}
