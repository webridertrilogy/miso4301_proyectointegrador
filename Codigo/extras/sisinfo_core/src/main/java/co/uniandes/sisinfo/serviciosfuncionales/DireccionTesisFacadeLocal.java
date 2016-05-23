/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.DireccionTesis;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Asistente
 */
@Local
public interface DireccionTesisFacadeLocal {

    void create(DireccionTesis direccionTesis);

    void edit(DireccionTesis direccionTesis);

    void remove(DireccionTesis direccionTesis);

    DireccionTesis find(Object id);

    List<DireccionTesis> findAll();

    List<DireccionTesis> findRange(int[] range);

    int count();

    DireccionTesis findDireccionTesisByName(String name);

}
