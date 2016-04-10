/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.uniandes.sisinfo.serviciosfuncionales;

import co.uniandes.sisinfo.entities.DireccionTesis;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Asistente
 */
@Remote
public interface DireccionTesisFacadeRemote {

    void create(DireccionTesis direccionTesis);

    void edit(DireccionTesis direccionTesis);

    void remove(DireccionTesis direccionTesis);

    DireccionTesis find(Object id);

    List<DireccionTesis> findAll();

    List<DireccionTesis> findRange(int[] range);

    int count();

    DireccionTesis findDireccionTesisByName(String name);

}
