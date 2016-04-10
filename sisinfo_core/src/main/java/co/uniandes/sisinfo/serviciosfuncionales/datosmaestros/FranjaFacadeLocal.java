/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales.datosmaestros;

import co.uniandes.sisinfo.entities.datosmaestros.Franja;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author da-naran
 */
@Local
public interface FranjaFacadeLocal {

    void create(Franja franja);

    void edit(Franja franja);

    void remove(Franja franja);

    Franja find(Object id);

    List<Franja> findAll();

    List<Franja> findRange(int[] range);

    int count();

}
